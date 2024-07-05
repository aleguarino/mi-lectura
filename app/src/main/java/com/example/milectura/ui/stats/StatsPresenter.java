/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: presenter obtiene las estadísticas      *
 * de lectura por el usuario de la base de datos según  *
 * el modo indicado en StatsFragment                    *
 ********************************************************/
package com.example.milectura.ui.stats;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.milectura.data.model.ReadingDate;
import com.example.milectura.data.repository.ReadingRepository;

public class StatsPresenter implements StatsContract.Presenter {

    private StatsContract.View view;

    public StatsPresenter(StatsContract.View view) {
        this.view = view;
    }

    @Override
    public LinkedHashMap<Long, Integer> getPagesWeekly() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long firstDay = cal.getTimeInMillis();
        for (int i = 0; i < 7; i++) {
            long day = cal.getTimeInMillis();
            cal.add(Calendar.DATE, 1);
        }
        long lastDay = cal.getTimeInMillis();
        List<ReadingDate> readings = ReadingRepository.getInstance().getPagesWeekly(firstDay, lastDay);
        HashMap<Long, Integer> pagesReadedWeekly = new HashMap<>();
        LinkedHashMap<Long, Integer> pagesByDayWeekly = new LinkedHashMap<>();
        if (readings.size() > 0) {
            for (ReadingDate r : readings) {
                pagesReadedWeekly.put(r.date, r.readed);
            }
            cal.add(Calendar.DAY_OF_MONTH, -7);
            for (int i = 0; i < 7; i++) {
                long day = cal.getTimeInMillis();
                int pages = 0;
                if (pagesReadedWeekly.containsKey(day))
                    pages = pagesReadedWeekly.get(day);
                pagesByDayWeekly.put(day, pages);
                cal.add(Calendar.DATE, 1);
            }
        }
        return pagesByDayWeekly;
    }

    @Override
    public LinkedHashMap<Long, Integer> getPagesMonthly() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long firstDay = cal.getTimeInMillis();
        cal.add(Calendar.DATE, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
        long lastDay = cal.getTimeInMillis();
        List<ReadingDate> readings = ReadingRepository.getInstance().getPagesWeekly(firstDay, lastDay);
        HashMap<Long, Integer> pagesReadedMonthly = new HashMap<>();
        LinkedHashMap<Long, Integer> pagesByDayMonthly = new LinkedHashMap<>();
        if (readings.size() > 0) {
            for (ReadingDate r : readings) {
                pagesReadedMonthly.put(r.date, r.readed);
            }
            cal.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 0; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                long day = cal.getTimeInMillis();
                int pages = 0;
                if (pagesReadedMonthly.containsKey(day))
                    pages = pagesReadedMonthly.get(day);
                pagesByDayMonthly.put(day, pages);
                cal.add(Calendar.DATE, 1);
            }
        }
        return pagesByDayMonthly;
    }

    @Override
    public LinkedHashMap<Long, Integer> getPagesYearly() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<ReadingDate> readings = ReadingRepository.getInstance().getPagesYearly(String.valueOf(year));
        HashMap<Long, Integer> pagesReadedMonthly = new HashMap<>();
        LinkedHashMap<Long, Integer> pagesByMonth = new LinkedHashMap<>();
        if (readings.size() > 0) {
            for (ReadingDate r : readings) {
                pagesReadedMonthly.put(r.date, r.readed);
            }
            for (long i = 1; i <= 12; i++) {
                int pages = 0;
                if (pagesReadedMonthly.containsKey(i))
                    pages = pagesReadedMonthly.get(i);
                pagesByMonth.put(i, pages);
            }
        }
        return pagesByMonth;
    }
}

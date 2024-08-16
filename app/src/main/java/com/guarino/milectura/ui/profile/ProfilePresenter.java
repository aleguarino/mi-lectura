/***************************************************
 * Autor: Alejandro Guarino Muñoz                  *
 *                                                 *
 * Descripcion: presenter que obtiene todas las    *
 * estadísticas recogidas a lo largo del uso de la *
 * aplicación por el usuario                       *
 ***************************************************/
package com.guarino.milectura.ui.profile;

import java.util.Calendar;
import java.util.List;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.ReadingDate;
import com.guarino.milectura.data.repository.BookRepository;
import com.guarino.milectura.data.repository.ReadingRepository;

public class ProfilePresenter implements ProfileContract.Presenter{

    private ProfileContract.View view;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public int getBookReaded() {
        List<Book> booksReaded = BookRepository.getInstance().getBooksReaded();
        return booksReaded.size();
    }

    @Override
    public int getDaysReading() {
        int days = 0;
        boolean reading = true;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long lastDay = cal.getTimeInMillis();
        Calendar cal2  = (Calendar) cal.clone();
        while (reading) {
            cal2.set(Calendar.DAY_OF_MONTH, 1);
            long firstDay = cal2.getTimeInMillis();
            List<ReadingDate> readings = ReadingRepository.getInstance().getPagesWeekly(firstDay, lastDay);
            while (cal.getTimeInMillis() >= firstDay && reading == true) {
                ReadingDate r = new ReadingDate();
                r.date = cal.getTimeInMillis();
                if (readings.contains(r)) {
                    cal.add(Calendar.DATE, -1);
                    days++;
                } else {
                    reading = false;
                }
            }
            cal2.add(Calendar.MONTH, -1);
            lastDay = cal.getTimeInMillis();
        }
        return days;
    }

    @Override
    public int getTotalBooks() {
        return BookRepository.getInstance().getList().size();
    }

    @Override
    public int getTotalDaysReading() {
        return ReadingRepository.getInstance().getTotalDaysReading();
    }
}

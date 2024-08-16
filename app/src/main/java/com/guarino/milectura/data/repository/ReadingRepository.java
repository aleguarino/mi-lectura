/*****************************************************
 * Autor: Alejandro Guarino Muñoz                    *
 *                                                   *
 * Descripcion: repositorio sobre la entidad Reading *
 *****************************************************/
package com.guarino.milectura.data.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.guarino.milectura.data.dao.ReadingDao;
import com.guarino.milectura.data.db.MiLecturaDatabase;
import com.guarino.milectura.data.model.Reading;
import com.guarino.milectura.data.model.ReadingDate;

public class ReadingRepository {

    private ReadingDao readingDao;
    private static ReadingRepository instance;

    static {
        instance = new ReadingRepository();
    }

    private ReadingRepository() {
        readingDao = MiLecturaDatabase.getDatabase().readingDao();
    }

    public static ReadingRepository getInstance() {
        return instance;
    }

    public long insert(Reading reading) {
        long rowId = -1;
        try {
            rowId = MiLecturaDatabase.databaseWriteExecutor.submit(() -> readingDao.insert(reading)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return rowId;
        }
    }

    public void update(Reading reading) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> readingDao.update(reading));
    }

    public void delete(Reading reading) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> readingDao.delete(reading));
    }

    public List<ReadingDate> getPagesWeekly(long firstDay, long lastDay)  {
        List<ReadingDate> pagesWeekly = null;
        try {
            pagesWeekly = MiLecturaDatabase.databaseWriteExecutor.submit(() -> readingDao.getPagesWeekly(firstDay, lastDay)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return pagesWeekly;
        }
    }

    public List<ReadingDate> getPagesYearly(String year)  {
        List<ReadingDate> pagesYearly = null;
        try {
            pagesYearly = MiLecturaDatabase.databaseWriteExecutor.submit(() -> readingDao.getPagesYearly(year)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return pagesYearly;
        }
    }

    public int getTotalDaysReading() {
        int days = 0;
        try {
            days = MiLecturaDatabase.databaseWriteExecutor.submit(() -> readingDao.getTotalDaysReading()).get().size();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return days;
        }
    }

}

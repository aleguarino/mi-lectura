/*************************************************************
 * Autor: Alejandro Guarino Muñoz                            *
 *                                                           *
 * Descripcion: DAO que hace referencia a la entidad Reading *
 *************************************************************/
package com.guarino.milectura.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.guarino.milectura.data.model.Reading;
import com.guarino.milectura.data.model.ReadingDate;

@Dao
public interface ReadingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Reading reading);

    @Update
    void update(Reading reading);

    @Delete
    void delete(Reading reading);

    @Query("SELECT SUM(readed) as readed, date FROM Reading WHERE date >= :firstDay and date <= :lastDay  GROUP BY date")
    List<ReadingDate> getPagesWeekly(long firstDay, long lastDay);

    @Query("SELECT SUM(readed) as readed, date FROM Reading WHERE date >= :firstDay and date <= :lastDay  GROUP BY date")
    List<ReadingDate> getPagesMonthly(long firstDay, long lastDay);

    @Query("SELECT sum(readed) as readed, strftime('%m', date/1000, 'unixepoch', 'localtime') as date FROM Reading where strftime('%Y', date/1000, 'unixepoch', 'localtime') = :year GROUP BY strftime('%Y-%m', date/1000, 'unixepoch', 'localtime')")
    List<ReadingDate> getPagesYearly(String year);

    @Query("SELECT count(*) as readed, date FROM Reading GROUP BY date")
    List<ReadingDate> getTotalDaysReading();

    @Query("DELETE FROM Reading")
    void deleteAll();
}

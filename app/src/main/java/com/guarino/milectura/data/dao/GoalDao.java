/**********************************************************
 * Autor: Alejandro Guarino Muñoz                         *
 *                                                        *
 * Descripcion: DAO que hace referencia a la entidad Goal *
 **********************************************************/
package com.guarino.milectura.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.guarino.milectura.data.model.Goal;

@Dao
public interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Goal goal);

    @Delete
    void delete(Goal goal);

    @Update
    void update(Goal goal);

    @Query("SELECT goal.* FROM book INNER JOIN goal ON book.isbn = goal.bookId WHERE goal.bookId = :bookId")
    Goal getGoal(String bookId);

    @Query("DELETE FROM Goal")
    void deleteAll();
}

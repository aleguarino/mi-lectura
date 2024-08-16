/**********************************************************
 * Autor: Alejandro Guarino Muñoz                         *
 *                                                        *
 * Descripcion: DAO que hace referencia a la entidad Book *
 **********************************************************/
package com.guarino.milectura.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.BooksWithGoal;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Book book);

    @Delete
    void delete(Book book);

    @Update
    void update(Book book);

    @Query("SELECT * FROM book")
    List<Book> getAll();

    @Query("SELECT COUNT(*) FROM book")
    int rows();

    @Query("SELECT actualPage FROM Book WHERE isbn=:isbn")
    int getActualPageFromBook(String isbn);

    @Query("UPDATE Book SET actualPage = :actualPage, finished = :finished, modifiedAt = :modifiedAt WHERE isbn = :isbn")
    void updateActualPage(String isbn, int actualPage, boolean finished, long modifiedAt);

    @Query("SELECT * FROM book WHERE pages = actualPage")
    List<Book> getBooksReaded();

    @Query("SELECT pages FROM book WHERE isbn = :isbn")
    int getPages(String isbn);

    @Transaction
    @Query("SELECT * FROM book INNER JOIN goal ON book.isbn = goal.bookId")
    List<BooksWithGoal> getBooksWithGoal();

    @Query("DELETE FROM Book")
    void deleteAll();


}

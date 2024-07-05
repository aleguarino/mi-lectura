/****************************************************
 * Autor: Alejandro Guarino Muñoz                   *
 *                                                  *
 * Descripcion: repositorio sobre la entidad Book   *
 ****************************************************/
package com.example.milectura.data.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.milectura.data.dao.BookDao;
import com.example.milectura.data.db.MiLecturaDatabase;
import com.example.milectura.data.model.Book;
import com.example.milectura.data.model.BooksWithGoal;

public class BookRepository {
    private static BookRepository instance;
    private BookDao bookDao;

    static {
        instance = new BookRepository();
    }

    private BookRepository() {
        bookDao = MiLecturaDatabase.getDatabase().bookDao();
    }

    public static BookRepository getInstance() {
        return instance;
    }

    public long insert(final Book book) {
        long rowId = -1;
        try {
            rowId = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.insert(book)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return rowId;
        }
    }

    public List<Book> getList() {
        List<Book> list = null;
        try {
            list = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.getAll()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public int getActualPageFromBook(String isbn) {
        int actualPage = 0;
        try {
            actualPage = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.getActualPageFromBook(isbn)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return actualPage;
        }
    }

    public List<Book> getBooksReaded() {
        List<Book> list = null;
        try {
            list = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.getBooksReaded()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public int getPages(String isbn) {
        int pages = 0;
        try {
            pages = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.getPages(isbn)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return pages;
        }
    }

    public List<BooksWithGoal> getBooksWithGoal() {
        List<BooksWithGoal> list = null;
        try {
            list = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.getBooksWithGoal()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public void update(Book book) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> bookDao.update(book));
    }

    public void updateActualPage(String isbn, int actualPage, boolean finished, long modifiedAt) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> bookDao.updateActualPage(isbn, actualPage, finished, modifiedAt));
    }

    public void delete (Book book) {
        MiLecturaDatabase.databaseWriteExecutor.execute(() -> bookDao.delete(book));
    }

    public int rows() {
        int rows = 0;
        try {
            rows = MiLecturaDatabase.databaseWriteExecutor.submit(() -> bookDao.rows()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return rows;
        }
    }
}

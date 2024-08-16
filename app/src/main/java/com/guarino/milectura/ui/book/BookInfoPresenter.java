/*********************************************************
 * Autor: Alejandro Guarino Muñoz                        *
 *                                                       *
 * Descripcion: presenter encargado de insertar y borrar *
 * libros, insertar, borrar y obtener goals              *
 *********************************************************/
package com.guarino.milectura.ui.book;

import java.util.Calendar;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.Goal;
import com.guarino.milectura.data.repository.BookRepository;
import com.guarino.milectura.data.repository.GoalRepository;

public class BookInfoPresenter implements BookInfoContract.Presenter {

    private BookInfoContract.View view;

    public BookInfoPresenter(BookInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void insert(Book book) {
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date  = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        book.setModifiedAt(System.currentTimeMillis());
        BookRepository.getInstance().insert(book);
        view.onSuccess();
    }

    @Override
    public void delete(Book book) {
        BookRepository.getInstance().delete(book);
        view.onSuccess();
    }

    @Override
    public void insertGoal(Goal goal) {
        GoalRepository.getInstance().insert(goal);
    }

    @Override
    public Goal getGoal(String isbn) {
        return GoalRepository.getInstance().getGoal(isbn);
    }

    @Override
    public void deleteGoal(Goal goal) {
        GoalRepository.getInstance().delete(goal);
    }
}

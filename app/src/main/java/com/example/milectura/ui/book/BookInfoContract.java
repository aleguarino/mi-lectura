/***********************************************************
 * Autor: Alejandro Guarino Muñoz                          *
 *                                                         *
 * Descripcion: interfaz para el mvp de BookInfoFragment   *
 ***********************************************************/
package com.example.milectura.ui.book;

import com.example.milectura.data.model.Book;
import com.example.milectura.data.model.Goal;

public interface BookInfoContract {
    interface View {
        void onSuccess();
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void insert(Book book);
        void delete(Book book);
        void insertGoal(Goal goal);
        Goal getGoal(String isbn);
        void deleteGoal(Goal goal);
    }
}

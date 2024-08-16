/***********************************************************
 * Autor: Alejandro Guarino Muñoz                          *
 *                                                         *
 * Descripcion: interfaz para el mvp de BookInfoFragment   *
 ***********************************************************/
package com.guarino.milectura.ui.book;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.Goal;

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

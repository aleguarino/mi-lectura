/**************************************************************
 * Autor: Alejandro Guarino Muñoz                             *
 *                                                            *
 * Descripcion: interfaz para el mvp de LibraryListFragment   *
 **************************************************************/
package com.example.milectura.ui.library;

import java.util.Date;
import java.util.List;

import com.example.milectura.data.model.Book;

public interface LibraryListContract {
    interface View {
        void onSuccess(List<Book> list);
        void setPresenter(Presenter presenter);
        void showError(int error);
        void updateProgress(int progress);
    }

    interface Presenter {
        void load();
        int getProgress(String bookId);
        void updateProgress(Book book, Date date, int actualPage, boolean finished);
    }
}

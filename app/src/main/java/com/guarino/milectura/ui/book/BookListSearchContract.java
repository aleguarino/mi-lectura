/***********************************************************
 * Autor: Alejandro Guarino Muñoz                          *
 *                                                         *
 * Descripcion: interfaz para el mvp de BookListSearch     *
 ***********************************************************/
package com.guarino.milectura.ui.book;

import java.util.List;

import com.guarino.milectura.data.model.Book;

public interface BookListSearchContract {
    interface View {
        boolean isVisibleImgNoData();
        void showImgNoData();
        void hideImgNoData();
        void onSuccess(List<Book> list);
        void showError(int error);
        void setPresenter(Presenter presenter);
    }
    interface Presenter {
        void findBook(String bookQuery);
    }
}

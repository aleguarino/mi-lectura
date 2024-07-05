/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: interfaz para el mvp de ProfileFragment *
 ********************************************************/
package com.example.milectura.ui.profile;

public interface ProfileContract {
    interface View {
        void onSuccess();
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        int getBookReaded();
        int getDaysReading();
        int getTotalBooks();
        int getTotalDaysReading();
    }
}

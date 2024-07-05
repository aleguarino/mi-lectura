/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: interfaz para el mvp de GoalsFragment   *
 ********************************************************/
package com.example.milectura.ui.goals;

import java.util.HashMap;
import java.util.List;

import com.example.milectura.data.model.BooksWithGoal;

public interface GoalsContract {
    interface View {
        void onSuccess(HashMap<String, List<BooksWithGoal>> map);
        void setPresenter(Presenter presenter);
        String setTitle(int id);
    }
    interface Presenter {
        void load();
    }
}

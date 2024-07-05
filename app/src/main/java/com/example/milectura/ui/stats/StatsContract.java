/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: interfaz para el mvp de StatsFragment   *
 ********************************************************/
package com.example.milectura.ui.stats;

import java.util.LinkedHashMap;


public interface StatsContract {

    interface View {
        void onSuccess();
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        LinkedHashMap<Long, Integer> getPagesWeekly();
        LinkedHashMap<Long, Integer> getPagesMonthly();
        LinkedHashMap<Long, Integer> getPagesYearly();
    }

}


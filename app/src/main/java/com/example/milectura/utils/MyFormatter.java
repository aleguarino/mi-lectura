/****************************************************************
 * Autor: Alejandro Guarino Muñoz                               *
 *                                                              *
 * Descripcion: clase encargada de dar formato a los gráficos   *
 ****************************************************************/
package com.example.milectura.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        if(value > 0) {
            return String.valueOf((int) value);
        } else {
            return "";
        }
    }
}

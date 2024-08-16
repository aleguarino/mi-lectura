/**********************************************
 * Autor: Alejandro Guarino Muñoz             *
 *                                            *
 * Descripcion: clase POJO de Reading         *
 **********************************************/
package com.guarino.milectura.data.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;

public class ReadingDate {
    @ColumnInfo(name = "readed")
    public int readed;

    @ColumnInfo(name = "date")
    public long date;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return this.date == ((ReadingDate)obj).date;
    }
}

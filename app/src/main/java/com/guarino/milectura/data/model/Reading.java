/*************************************************
 * Autor: Alejandro Guarino Muñoz                *
 *                                               *
 * Descripcion: definición de la entidad Reading *
 *************************************************/
package com.guarino.milectura.data.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Book.class,
        parentColumns = "isbn",
        childColumns = "bookId",
        onDelete = CASCADE))
public class Reading {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String bookId;
    @NonNull
    private Date date;
    @NonNull
    private int readed;

    public Reading(String bookId, Date date, int readed) {
        this.bookId = bookId;
        this.date = date;
        this.readed = readed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

}

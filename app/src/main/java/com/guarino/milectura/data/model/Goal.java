/**********************************************
 * Autor: Alejandro Guarino Muñoz             *
 *                                            *
 * Descripcion: definición de la entidad Goal *
 **********************************************/
package com.guarino.milectura.data.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Book.class,
        parentColumns = "isbn",
        childColumns = "bookId",
        onDelete = CASCADE),
        indices = {@Index(value = {"bookId"}, unique = true)})
public class Goal {
    public static final String TAG = "Goal";
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String bookId;
    @NonNull
    private Date targetDate;
    @NonNull
    private int remainingPages;
    private boolean completed;

    public Goal(@NonNull String bookId, @NonNull Date targetDate, int remainingPages) {
        this.bookId = bookId;
        this.targetDate = targetDate;
        this.remainingPages = remainingPages;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @NonNull
    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(@NonNull Date targetDate) {
        this.targetDate = targetDate;
    }

    public int getRemainingPages() {
        return remainingPages;
    }

    public void setRemainingPages(int remainingPages) {
        this.remainingPages = remainingPages;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

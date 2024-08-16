/**********************************************
 * Autor: Alejandro Guarino Muñoz             *
 *                                            *
 * Descripcion: definición de la relación de  *
 * una a uno entre Book y Goal                *
 **********************************************/
package com.guarino.milectura.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BooksWithGoal {
    @Embedded
    Book book;
    @Relation(
            parentColumn = "isbn",
            entityColumn = "bookId"
    )
    Goal goal;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}

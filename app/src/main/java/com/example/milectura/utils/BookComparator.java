package com.example.milectura.utils;

import com.example.milectura.data.model.Book;

import java.util.Comparator;

public class BookComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        //if (b1.getAdded() < b2.getAdded()) return 1;
        return -1;
    }
}

/**********************************************
 * Autor: Alejandro Guarino Muñoz             *
 *                                            *
 * Descripcion: definición de la entidad Book *
 **********************************************/
package com.example.milectura.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"isbn"}, unique = true) })
public class Book implements Parcelable, Comparable<Book> {
    public static final String TAG = "Book";
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private String isbn;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private int pages;
    @NonNull
    private int actualPage;
    @NonNull
    private String thumbnail;
    private String description;
    @NonNull
    private Date added;
    @NonNull
    private boolean finished = false;
    private Long modifiedAt;

    public Book(String isbn, @NonNull String title, @NonNull String author, int pages, int actualPage, String thumbnail, String description, Date added) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.actualPage = actualPage;
        this.thumbnail = thumbnail;
        this.description = description;
        this.added = added;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        title = in.readString();
        author = in.readString();
        pages = in.readInt();
        actualPage = in.readInt();
        thumbnail = in.readString();
        description = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getActualPage() {
        return actualPage;
    }

    public void setActualPage(int actualPage) {
        this.actualPage = actualPage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "LIBRO: " + this.getTitle();
    }

    @Override
    public int describeContents() {
        return CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isbn);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(pages);
        dest.writeInt(actualPage);
        dest.writeString(thumbnail);
        dest.writeString(description);
    }

    @Override
    public int compareTo(Book b) {
        if (modifiedAt < b.modifiedAt)
            return 1;
        else if (modifiedAt > b.modifiedAt)
            return -1;
        return 0;
    }
}

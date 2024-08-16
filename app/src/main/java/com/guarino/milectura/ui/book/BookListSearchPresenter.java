/************************************************************
 * Autor: Alejandro Guarino Muñoz                           *
 *                                                          *
 * Descripcion: presenter encargado de recoger los libros   *
 * encontrados mediante el texto introducido en la búsqueda *
 * y devolverlo listado a BookListSearchFragment            *
 ************************************************************/
package com.guarino.milectura.ui.book;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.network.NetworkUtils;

public class BookListSearchPresenter implements BookListSearchContract.Presenter {

    private BookListSearchContract.View view;

    public BookListSearchPresenter(BookListSearchContract.View view) {
        this.view = view;
    }

    @Override
    public void findBook(String bookQuery) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //if (view.isVisibleImgNoData()) view.hideImgNoData();
                String bookInfo = NetworkUtils.getBookInfo(bookQuery);
                //Background work here

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<Book> list = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(bookInfo);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject book = jsonArray.getJSONObject(i);
                                String title = null;
                                String authors = null;
                                String description = null;
                                String isbn = null;
                                int pages = 0;
                                String thumbnail = null;
                                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                                try {
                                    title = volumeInfo.getString("title");
                                    authors = volumeInfo.getString("authors").replaceAll("[^A-Za-zÁÉÍÓÚáéíóúñÑ, ]", "").replaceAll("[,]", ", ");
                                    description = volumeInfo.getString("description");
                                    pages = volumeInfo.getInt("pageCount");
                                    thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                                    StringBuilder stringBuilder = new StringBuilder(thumbnail);
                                    stringBuilder.insert(4, "s");
                                    isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(1).getString("identifier");
                                    //Log.d("TEST", String.valueOf(isbn));
                                    Date date = new Date();
                                    list.add(new Book(isbn, title, authors, pages, 0, stringBuilder.toString(), description, date));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            view.hideImgNoData();
                            if (list.isEmpty()) view.showImgNoData();
                            view.onSuccess(list);
                        }
                        //UI Thread work here

                    }
                });
            }
        });

    }
}

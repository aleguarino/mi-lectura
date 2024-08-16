/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: activity encargada de mostrar los       *
 * fragments BookInfoFragment y BookSearchFragment      *
 ********************************************************/
package com.guarino.milectura.ui.book;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.Book;

public class BookSearchActivity extends AppCompatActivity implements BookListSearchFragment.OnManageBookListSearch {

    public static final String TAG = "BookSearchActivity";
    private BookListSearchFragment bookListSearchFragment;
    private BookListSearchPresenter bookListSearchPresenter;
    private BookInfoFragment bookInfoFragment;
    private BookInfoPresenter bookInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        showBookListSearchFragment();
    }

    private void showBookListSearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        bookListSearchFragment = (BookListSearchFragment) fragmentManager.findFragmentByTag(BookListSearchFragment.TAG);
        if (bookListSearchFragment == null) {
            bookListSearchFragment = (BookListSearchFragment) BookListSearchFragment.newInstance(null);
        }
        bookListSearchPresenter = new BookListSearchPresenter(bookListSearchFragment);
        bookListSearchFragment.setPresenter(bookListSearchPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, bookListSearchFragment).commit();
    }

    private void showBookInfoFragment(Book book) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        bookInfoFragment = (BookInfoFragment) fragmentManager.findFragmentByTag(BookInfoFragment.TAG);
        if (bookInfoFragment == null) {
            Bundle bundle = null;
            if (book != null) {
                bundle = new Bundle();
                bundle.putParcelable(Book.TAG, book);
            }
            bookInfoFragment = (BookInfoFragment) BookInfoFragment.newInstance(bundle);
        }
        bookInfoPresenter = new BookInfoPresenter(bookInfoFragment);
        bookInfoFragment.setPresenter(bookInfoPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, bookInfoFragment).addToBackStack(null).commit();
    }

    @Override
    public void showBookInfo(Book book) {
        showBookInfoFragment(book);
    }

    @Override
    public void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}

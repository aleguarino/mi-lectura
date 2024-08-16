/*********************************************************
 * Autor: Alejandro Guarino Muñoz                        *
 *                                                       *
 * Descripcion: fragment que muestra el listado obtenido *
 * al buscar un libro para añadir a la biblioteca        *
 *********************************************************/
package com.guarino.milectura.ui.book;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.adapter.BookSearchAdapter;
import com.guarino.milectura.data.model.Book;

public class BookListSearchFragment extends Fragment implements BookListSearchContract.View {
    public static final String TAG = "BookListSearchFragment";

    private EditText etSearch;
    private RecyclerView rvSearchBook;
    private BookListSearchContract.Presenter presenter;
    private BookSearchAdapter bookAdapter;
    private ProgressBar pbSearchBook;
    private BookSearchAdapter.OnManageBookInfoListener bookInfoListener;
    private Toolbar toolbar;
    private RelativeLayout rlNoData;

    private OnManageBookListSearch listener;

    public static Fragment newInstance(Bundle bundle) {
        BookListSearchFragment fragment = new BookListSearchFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_list_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = view.findViewById(R.id.etSearch);
        rlNoData = view.findViewById(R.id.rlNoData);
        rvSearchBook = view.findViewById(R.id.rvSearchBook);
        pbSearchBook = view.findViewById(R.id.pbBookSearch);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        etSearch.requestFocus();
        initEtSearch();
        initRecycler();
    }

    private void initRecycler() {
        initBookInfoListener();
        bookAdapter = new BookSearchAdapter(bookInfoListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvSearchBook.setLayoutManager(linearLayoutManager);
        rvSearchBook.setAdapter(bookAdapter);
    }

    private void initBookInfoListener() {
        bookInfoListener = new BookSearchAdapter.OnManageBookInfoListener() {
            @Override
            public void showBook(Book book) {
                listener.showBookInfo(book);
            }
        };
    }

    private void clearPreviousData() {
        showProgressBar();
        bookAdapter.clear();
        bookAdapter.notifyDataSetChanged();
    }

    private void showProgressBar() {
        pbSearchBook.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        pbSearchBook.setVisibility(View.GONE);
    }

    private void initEtSearch() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Todo: solucionar error al buscar con texto vacío y mostrar imagen cuando no haya ningún libro listado
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listener.closeKeyboard();
                    clearPreviousData();
                    presenter.findBook(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnManageBookListSearch) {
            listener = (OnManageBookListSearch) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnManageBookListSearch");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSuccess(List<Book> list) {
        bookAdapter.clear();
        bookAdapter.load(list);
        bookAdapter.notifyDataSetChanged();
        hideProgressBar();
    }

    @Override
    public void showImgNoData() {
        rlNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImgNoData() {
        rlNoData.setVisibility(View.GONE);
    }

    @Override
    public boolean isVisibleImgNoData() {
        return rlNoData.getVisibility()==View.VISIBLE;
    }

    @Override
    public void showError(int error) {

    }

    @Override
    public void setPresenter(BookListSearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public interface OnManageBookListSearch {
        void showBookInfo(Book book);
        void closeKeyboard();
    }
}

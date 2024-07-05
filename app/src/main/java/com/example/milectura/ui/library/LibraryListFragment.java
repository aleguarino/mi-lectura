/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: fragment que muestra el listado de      *
 * los libros del usuario                               *
 ********************************************************/
package com.example.milectura.ui.library;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.milectura.data.network.NetworkUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.example.milectura.R;
import com.example.milectura.adapter.LibraryAdapter;
import com.example.milectura.data.model.Book;
import com.example.milectura.utils.BookComparator;

public class LibraryListFragment extends Fragment implements LibraryListContract.View {

    public static final String TAG = "LibraryListFragment";
    public static final String SELECTION = "Selection";

    private FloatingActionButton fab;
    private LinearLayout layoutEmptyLibrary;
    private LinearLayout linearLayout;
    private LibraryAdapter adapter;
    private LibraryListContract.Presenter presenter;
    private ViewPager2 viewPager2;
    private TextView bookTitle;
    private NumberProgressBar bookProgressBar;
    private Button btUpdateBook;
    private DatePickerDialog datePickerDialog;
    private LibraryAdapter.OnManageLibraryListener bookInfoListener;
    private Book book = null;


    private OnManageLibraryListener listener;

    public static Fragment newInstance(Bundle bundle) {
        LibraryListFragment fragment = new LibraryListFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.viewPager);
        fab = view.findViewById(R.id.fab);
        layoutEmptyLibrary = view.findViewById(R.id.layoutEmptyLibrary);
        linearLayout = view.findViewById(R.id.linearLayout);
        bookTitle = view.findViewById(R.id.bookTitle);
        bookProgressBar = view.findViewById(R.id.bookProgressBar);
        btUpdateBook = view.findViewById(R.id.btUpdateBook);
        initFab();
        initRvLibrary();
        initBtUpdate();
    }

    //todo deshabilitar actualizar si el libro está finalizado
    private void initBtUpdate() {

        btUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_update, null);
                builder.setView(customLayout);

                EditText dialog_actualPage = customLayout.findViewById(R.id.dialog_actualPage);
                EditText dialog_totalPages = customLayout.findViewById(R.id.dialog_totalPages);
                EditText dialogDate = customLayout.findViewById(R.id.dialog_date);
                CheckBox dialog_finish = customLayout.findViewById(R.id.dialog_finish);
                SimpleDraweeView dialog_thumbnail = customLayout.findViewById(R.id.dialog_thumbnail);
                dialog_thumbnail.setImageURI(book.getThumbnail());

                //Todo: mostrar titulo del libro en el cuadro de dialogo
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                ((EditText) dialogDate).setText(String.format("%02d/%02d/%04d", day, (month + 1), year));
                dialog_thumbnail.setImageURI(book.getThumbnail());
                dialog_actualPage.setText(String.valueOf(presenter.getProgress(book.getIsbn())));

                initDatePicker(dialogDate);

                dialog_totalPages.setText(String.valueOf(book.getPages()));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (Integer.parseInt(dialog_actualPage.getText().toString()) <= bookProgressBar.getMax() && sdf.parse(dialogDate.getText().toString()).getTime() <= calendar.getTimeInMillis()) {
                                Date date = null;
                                date = sdf.parse(dialogDate.getText().toString());
                                Log.d("Fecha update", date.toString());
                                if (Integer.parseInt(dialog_actualPage.getText().toString()) != 0 || dialog_finish.isChecked())
                                    presenter.updateProgress(book,
                                            date,
                                            Integer.parseInt(dialog_actualPage.getText().toString()), dialog_finish.isChecked());
                            } else if (Integer.parseInt(dialog_actualPage.getText().toString()) > bookProgressBar.getMax())
                                Toast.makeText(getContext(), "La página introducida no puede ser mayor al total de páginas", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "La fecha introducida no puede ser superior a la actual", Toast.LENGTH_SHORT).show();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initDatePicker(View dialogDate) {
        dialogDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                ((EditText) dialogDate).setText(String.format("%02d/%02d/%04d", dayOfMonth, (month + 1), year));
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.load();
    }

    private void initRvLibrary() {
        initBookInfoListener();
        adapter = new LibraryAdapter(viewPager2, bookInfoListener);
        viewPager2.setAdapter(adapter);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                book = adapter.getBook(position);
                bookTitle.setText(book.getTitle());
                bookProgressBar.setMax(book.getPages());
                bookProgressBar.setProgress(presenter.getProgress(book.getIsbn()));
                if (book.isFinished())
                    btUpdateBook.setVisibility(View.INVISIBLE);
                else
                    btUpdateBook.setVisibility(View.VISIBLE);
                Log.d("libro", book.toString());
                Log.d("libro", "Progreso: " + bookProgressBar.getProgress());
                Log.d("libro", "Máximo: " + bookProgressBar.getMax());
            }
        });
    }


    private void initBookInfoListener() {
        bookInfoListener = new LibraryAdapter.OnManageLibraryListener() {
            @Override
            public void showBook(Book book) {
                listener.loadInfoBook(book);
            }
        };
    }

    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getContext())) {
                        listener.loadSearchBook();
                } else {
                    Toast.makeText(getContext(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnManageLibraryListener) {
            listener = (OnManageLibraryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnManageLibraryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSuccess(List<Book> list) {
        Collections.sort(list, new BookComparator());
        Collections.sort(list, new BookComparator());
        Book b = null;
        adapter.clear();
        adapter.load(list);
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            layoutEmptyLibrary.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        } else {
            layoutEmptyLibrary.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(LibraryListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showError(int error) {

    }

    @Override
    public void updateProgress(int progress) {
        bookProgressBar.setProgress(progress);
        if (bookProgressBar.getMax() == progress) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 29) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }

    public interface OnManageLibraryListener {
        void loadSearchBook();

        void update(Book book);

        void loadInfoBook(Book book);
    }
}

/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: fragment que muestra toda la           *
 * información recogida del libro                       *
 ********************************************************/
package com.guarino.milectura.ui.book;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.guarino.milectura.R;
import com.guarino.milectura.data.broadcast.ReminderBroadcast;
import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.data.model.Goal;
import com.guarino.milectura.ui.MainActivity;
import com.guarino.milectura.ui.library.LibraryListFragment;

public class BookInfoFragment extends Fragment implements BookInfoContract.View {

    public static final String TAG = "BookInfoFragment";
    public static final String INFO = "Info";
    private static final int MAX_LINES = 5;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private TextView book_description;
    private TextView book_title;
    private TextView book_author;
    private TextView book_pages;
    private TextView book_isbn;
    private SimpleDraweeView book_thumbnail;
    private ImageButton btReadMoreDown;
    private ImageButton btReadMoreUp;
    private FloatingActionButton fab;
    private BookInfoContract.Presenter presenter;
    private Book book = null;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private int maxScrollSize;
    private boolean isAvatarShown = true;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public static Fragment newInstance(Bundle bundle) {
        BookInfoFragment fragment = new BookInfoFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null)
            if (!((Book) getArguments().getParcelable(Book.TAG)).isFinished())
                setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (getArguments().getBoolean(INFO, false)) {
            inflater.inflate(R.menu.book_info_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                loadSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadSettings() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        boolean hasGoal = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(customLayout);
        EditText etTime = customLayout.findViewById(R.id.etTime);
        initTimePicker(etTime);
        CheckBox dialogCBdate = customLayout.findViewById(R.id.dialog_cbdate);
        EditText dialogDate = customLayout.findViewById(R.id.dialog_date);
        dialogDate.setText(String.format("%02d/%02d/%04d", day, (month + 1), year));
        Goal goal = presenter.getGoal(book.getIsbn());
        if (goal != null) {
            dialogCBdate.setChecked(true);
            dialogDate.setText(sdf.format(goal.getTargetDate()));
            dialogDate.setVisibility(View.VISIBLE);
            hasGoal = true;
        }
        List<CheckBox> cbDayList = new ArrayList<>();
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbsunday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbmonday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbtuesday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbwednesday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbthursday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbfriday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbsaturday));
        checkExistingAlarmas(cbDayList);
        customLayout.findViewById(R.id.llPages).setVisibility(View.GONE);
        initDatePicker(dialogDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dialogCBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCBdate.isChecked()) dialogDate.setVisibility(View.VISIBLE);
                else dialogDate.setVisibility(View.GONE);
            }
        });
        Goal finalGoal = goal;
        boolean finalHasGoal = hasGoal;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Goal goal = null;
                if (dialogCBdate.isChecked()) {
                    try {
                        Date date = sdf.parse(dialogDate.getText().toString());
                        if (date.getTime() < calendar.getTimeInMillis()) {
                            Toast.makeText(getContext(), getString(R.string.date_outbound_message), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.settings_updated_message), Toast.LENGTH_SHORT).show();
                            goal = new Goal(book.getIsbn(), date, book.getPages());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (finalHasGoal) {
                    presenter.deleteGoal(finalGoal);
                }
                String[] split = etTime.getText().toString().split(":");
                createAlarm(cbDayList, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                if (goal != null)
                    presenter.insertGoal(goal);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        book_description = view.findViewById(R.id.book_description);
        book_thumbnail = view.findViewById(R.id.book_thumbnail);
        book_title = view.findViewById(R.id.book_title);
        book_author = view.findViewById(R.id.book_author);
        book_pages = view.findViewById(R.id.book_pages);
        book_isbn = view.findViewById(R.id.book_isbn);
        btReadMoreDown = view.findViewById(R.id.btReadMoreDown);
        btReadMoreUp = view.findViewById(R.id.btReadMoreUp);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        appBarLayout = view.findViewById(R.id.appbar);
        maxScrollSize = appBarLayout.getTotalScrollRange();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (maxScrollSize == 0)
                    maxScrollSize = appBarLayout.getTotalScrollRange();

                int percentage = (Math.abs(i)) * 100 / maxScrollSize;

                if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
                    isAvatarShown = false;

                    book_thumbnail.animate()
                            .scaleY(0).scaleX(0)
                            .setDuration(200)
                            .start();
                }

                if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
                    isAvatarShown = true;

                    book_thumbnail.animate()
                            .scaleY(1).scaleX(1)
                            .start();
                }
            }
        });
        fab = view.findViewById(R.id.fab);
        initFab();
        Bundle bundle = getArguments();
        if (bundle != null) {
            book = bundle.getParcelable(Book.TAG);
            loadInfoBook();
        }
        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(book.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initFab() {
        if (getArguments().getBoolean(INFO, false)) {
            fab.setImageResource(R.drawable.ic_delete);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST", String.valueOf(book.getIsbn()));
                    showDeleteDialog(book);
                }
            });
        } else {
            fab.setImageResource(R.drawable.ic_add);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST", String.valueOf(book.getIsbn()));
                    showAddDialog(book);
                }
            });
        }
    }

    private void showDeleteDialog(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.alert_title_delete);
        builder.setMessage("Está seguro de eliminar el libro " + book.getTitle());
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.delete(book);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showAddDialog(Book book) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(customLayout);
        EditText etTime = customLayout.findViewById(R.id.etTime);
        List<CheckBox> cbDayList = new ArrayList<>();
        CheckBox dialogCBdate = customLayout.findViewById(R.id.dialog_cbdate);
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbsunday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbmonday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbtuesday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbwednesday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbthursday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbfriday));
        cbDayList.add(customLayout.findViewById(R.id.dialog_cbsaturday));
        EditText dialogPages = customLayout.findViewById(R.id.dialog_pages);
        EditText dialogDate = customLayout.findViewById(R.id.dialog_date);
        dialogDate.setText(String.format("%02d/%02d/%04d", day, (month + 1), year));
        initDatePicker(dialogDate);
        initTimePicker(etTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dialogPages.setText(String.valueOf(book.getPages()));
        dialogCBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCBdate.isChecked()) dialogDate.setVisibility(View.VISIBLE);
                else dialogDate.setVisibility(View.GONE);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Goal goal = null;
                book.setPages(Integer.valueOf(dialogPages.getText().toString()));
                if (dialogCBdate.isChecked()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = sdf.parse(dialogDate.getText().toString());
                        if (date.getTime() < calendar.getTimeInMillis()) {
                            Toast.makeText(getContext(), "La fecha límite no puede ser anterior a la fecha actual", Toast.LENGTH_SHORT).show();
                        } else {
                            goal = new Goal(book.getIsbn(), date, book.getPages());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                presenter.insert(book);
                String[] split = etTime.getText().toString().split(":");
                createAlarm(cbDayList, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                if (goal != null)
                    presenter.insertGoal(goal);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAlarm(List<CheckBox> days, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        long today = c.getTimeInMillis();
        Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
        intent.putExtra(Book.TAG, book.getTitle());
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        for (int i = 0; i < days.size(); i++) {
            int day = i + 1;
            if (days.get(i).isChecked()) {
                c.set(Calendar.DAY_OF_WEEK, day);
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.valueOf((book.getId()) + String.valueOf(day)), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                if (today > c.getTimeInMillis()) {
                    c.add(Calendar.WEEK_OF_YEAR, 1);
                }
                Log.d("alarma " + book.getId(), "" + c.getTimeInMillis());
                Log.d("alarma rid", book.getId() + String.valueOf(day));
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            } else if ((pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.valueOf((book.getId()) + String.valueOf(day)), intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE)) != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }


    private void checkExistingAlarmas(List<CheckBox> days) {
        Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
        intent.putExtra(Book.TAG, book.getTitle());
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        for (int i = 0; i < days.size(); i++) {
            int day = i + 1;
            if ((pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.valueOf((book.getId()) + String.valueOf(day)), intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE)) != null) {
                days.get(i).setChecked(true);
            }
        }
    }


    private void loadInfoBook() {
        book_description.setText(book.getDescription());
        book_title.setText(book.getTitle());
        book_author.setText(book.getAuthor());
        book_pages.setText(String.valueOf(book.getPages()));
        book_isbn.setText(String.valueOf(book.getIsbn()));
        book_thumbnail.setImageURI(book.getThumbnail());
        initBtReadMoreDown();
        initBtReadMoreUp();
    }

    private void initBtReadMoreDown() {
        //Todo: ocultar boton si el texto no ha sido cortado
        btReadMoreDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btReadMoreDown.setVisibility(View.GONE);
                btReadMoreUp.setVisibility(View.VISIBLE);
                book_description.setMaxLines(Integer.MAX_VALUE);
            }
        });
    }

    private void initBtReadMoreUp() {
        btReadMoreUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btReadMoreUp.setVisibility(View.GONE);
                btReadMoreDown.setVisibility(View.VISIBLE);
                book_description.setMaxLines(MAX_LINES);
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

    private void initTimePicker(EditText etTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        etTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes));
        etTime.setInputType(InputType.TYPE_NULL);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                etTime.setText(String.format("%02d", sHour) + ":" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LibraryListFragment.SELECTION, book);
        startActivity(intent);
    }

    @Override
    public void setPresenter(BookInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }
}

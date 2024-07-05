/********************************************************
 * Autor: Alejandro Guarino Muñoz                       *
 *                                                      *
 * Descripcion: fragment que muestra el perfil del      *
 * usuario logueado o invitado                          *
 ********************************************************/
package com.example.milectura.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.milectura.R;
import com.example.milectura.data.db.MiLecturaDatabase;
import com.example.milectura.ui.login.LoginActivity;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    public static final String TAG = "ProfileFragment";

    private ProfileContract.Presenter presenter;
    private TextView tvUsername;
    private TextView tvBooksReaded;
    private TextView tvDaysReading;
    private TextView tvTotalBooks;
    private TextView tvDaysRead;
    private String name;
    private Toolbar toolbar;
    private SharedPreferences pref;

    public static Fragment newInstance(Bundle bundle) {
        ProfileFragment fragment = new ProfileFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        pref = this.getActivity().getSharedPreferences("shared_user_login", Context.MODE_PRIVATE);
        name = pref.getString("name", null);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_profile_menu, menu);
        if (pref.getBoolean("guestLogin", false)) {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.login).setVisible(true);
        } else {
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.login).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                loadSettings();
                return true;
            case R.id.login:
                signIn();
                return true;
            case R.id.logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void signIn() {
        getContext().getSharedPreferences("shared_user_login", getContext().MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.alert_logout_title);
        builder.setMessage(getString(R.string.alert_logout_message));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContext().getSharedPreferences("shared_user_login", getContext().MODE_PRIVATE).edit().clear().apply();
                AsyncTask.execute(() -> MiLecturaDatabase.getDatabase().clearAllTables());
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    private void loadSettings() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        tvUsername = view.findViewById(R.id.username);
        tvBooksReaded = view.findViewById(R.id.tvBooksReaded);
        tvDaysReading = view.findViewById(R.id.tvDaysReading);
        tvTotalBooks = view.findViewById(R.id.tvTotalBooks);
        tvDaysRead = view.findViewById(R.id.tvDaysRead);
        tvUsername.setText(name == "guest" ? "Invitado" : name);
        loadToolbar();
        loadData();
    }

    private void loadToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    private void loadData() {
        tvBooksReaded.setText(String.valueOf(presenter.getBookReaded()));
        tvDaysReading.setText(String.valueOf(presenter.getDaysReading()));
        tvTotalBooks.setText(String.valueOf(presenter.getTotalBooks()));
        tvDaysRead.setText(String.valueOf(presenter.getTotalDaysReading()));
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
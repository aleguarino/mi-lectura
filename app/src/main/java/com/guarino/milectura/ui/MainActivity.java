/***********************************************
 * Autor: Alejandro Guarino Muñoz              *
 *                                             *
 * Descripcion: activity principal encargada   *
 * de la navegación principal de la aplicación *
 ***********************************************/
package com.guarino.milectura.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.guarino.milectura.R;
import com.guarino.milectura.data.model.Book;
import com.guarino.milectura.ui.book.BookInfoFragment;
import com.guarino.milectura.ui.book.BookInfoPresenter;
import com.guarino.milectura.ui.book.BookSearchActivity;
import com.guarino.milectura.ui.goals.GoalsFragment;
import com.guarino.milectura.ui.goals.GoalsPresenter;
import com.guarino.milectura.ui.library.LibraryListFragment;
import com.guarino.milectura.ui.library.LibraryListPresenter;
import com.guarino.milectura.ui.profile.ProfileFragment;
import com.guarino.milectura.ui.profile.ProfilePresenter;
import com.guarino.milectura.ui.stats.StatsFragment;
import com.guarino.milectura.ui.stats.StatsPresenter;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements LibraryListFragment.OnManageLibraryListener {

    private static final int RC_NOTIFICATION = 99;
    private BottomNavigationView bottomNavigationView;
    private LibraryListFragment libraryListFragment;
    private LibraryListPresenter libraryListPresenter;
    private GoalsFragment goalsFragment;
    private GoalsPresenter goalsPresenter;
    private StatsFragment statsFragment;
    private StatsPresenter statsPresenter;
    private ProfileFragment profileFragment;
    private ProfilePresenter profilePresenter;
    private BottomNavigationView nav_view;
    private BookInfoFragment bookInfoFragment;
    private BookInfoPresenter bookInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);
        loadLibraryList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS}, RC_NOTIFICATION);
        }
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_library:
                        loadLibraryList();
                        break;
                    case R.id.navigation_goals:
                        loadGoalsFragment();
                        break;
                    case R.id.navigation_stats:
                        loadStatsFragment();
                        break;
                    case R.id.navigation_profile:
                        loadProfileFragment();
                        break;
                }
                return true;
            };
    //todo: borrar cola fragments
    private void loadGoalsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        goalsFragment = (GoalsFragment) fragmentManager.findFragmentByTag(GoalsFragment.TAG);
        if (goalsFragment == null) {
            goalsFragment = (GoalsFragment) GoalsFragment.newInstance(null);
        }
        goalsPresenter = new GoalsPresenter(goalsFragment);
        goalsFragment.setPresenter(goalsPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, goalsFragment).commit();
    }

    private void loadProfileFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        profileFragment = (ProfileFragment) fragmentManager.findFragmentByTag(ProfileFragment.TAG);
        if (profileFragment == null) {
            profileFragment = (ProfileFragment) ProfileFragment.newInstance(null);
        }
        profilePresenter = new ProfilePresenter(profileFragment);
        profileFragment.setPresenter(profilePresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, profileFragment).commit();
    }

    private void loadStatsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        statsFragment = (StatsFragment) fragmentManager.findFragmentByTag(StatsFragment.TAG);
        if (statsFragment == null) {
            statsFragment = (StatsFragment) StatsFragment.newInstance(null);
        }
        statsPresenter = new StatsPresenter(statsFragment);
        statsFragment.setPresenter(statsPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, statsFragment).commit();
    }

    private void loadLibraryList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        libraryListFragment = (LibraryListFragment) fragmentManager.findFragmentByTag(LibraryListFragment.TAG);
        if (libraryListFragment == null) {
            libraryListFragment = (LibraryListFragment) LibraryListFragment.newInstance(null);
        }
        libraryListPresenter = new LibraryListPresenter(libraryListFragment);
        libraryListFragment.setPresenter(libraryListPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, libraryListFragment).commit();
    }

    @Override
    public void loadSearchBook() {
        Intent intent = new Intent(MainActivity.this, BookSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void update(Book book) {

    }

    @Override
    public void loadInfoBook(Book book) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        bookInfoFragment = (BookInfoFragment) fragmentManager.findFragmentByTag(BookInfoFragment.TAG);
        if (bookInfoFragment == null) {
            Bundle bundle = null;
            if (book != null) {
                bundle = new Bundle();
                bundle.putParcelable(Book.TAG, book);
                bundle.putBoolean(BookInfoFragment.INFO, true);
            }
            bookInfoFragment = (BookInfoFragment) BookInfoFragment.newInstance(bundle);
        }
        bookInfoPresenter = new BookInfoPresenter(bookInfoFragment);
        bookInfoFragment.setPresenter(bookInfoPresenter);
        fragmentManager.beginTransaction().replace(R.id.containerFragment, bookInfoFragment).addToBackStack(null).commit();
    }

}

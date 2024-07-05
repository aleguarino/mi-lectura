/**************************************************************
 * Autor: Alejandro Guarino Muñoz                             *
 *                                                            *
 * Descripcion: splash activity que carga la actividad        *
 * de login o main en función de si el usuario se ha logueado *
 **************************************************************/
package com.example.milectura.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milectura.R;
import com.example.milectura.ui.MainActivity;
import com.example.milectura.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long WAIT_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                checkUserLogged();
            }

        };
        handler.postDelayed(runnable, WAIT_TIME);
    }

    private void initMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void initLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private void checkUserLogged() {
        SharedPreferences prefs = getSharedPreferences("shared_user_login", MODE_PRIVATE);
        if (prefs.getBoolean("hasLogin", false))
            initMain();
        else
            initLogin();
    }
}
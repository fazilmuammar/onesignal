package com.example.ekreasi.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.ekreasi.R;
import com.example.ekreasi.helper.SessionManager;


/**
 * Activity for SplashScreen layout resources
 *
 * Activity ini untuk menjalankan halaman SplashScreen
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */

public class SplashScreen extends AppCompatActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        /**
         * Method ini berfungsi untuk menjalankan halaman SplashScreen
         * Menggunakan handler untuk menghitung lama durasi saat halaman SplashScreen dijalankan
         */
        Handler handler = new Handler();

        manager = new SessionManager(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager.isLogin() == true){
                    startActivity(new Intent(SplashScreen.this,HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        },2000);


    }
}

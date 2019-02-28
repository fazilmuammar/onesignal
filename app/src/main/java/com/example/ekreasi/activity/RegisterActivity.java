package com.example.ekreasi.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.andreabaccega.widget.FormEditText;
import com.example.ekreasi.R;
import com.example.ekreasi.databinding.ActivityRegisterBinding;
import com.example.ekreasi.view.RegisterView;
import com.example.ekreasi.viewmodel.RegisterViewModel;

/**
 * Activity for Register layout resources
 *
 * Activity ini untuk user melakukan account sehingga user dapat login
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */

public class RegisterActivity extends AppCompatActivity {

    /**
     * variabel yang berfungsi untuk menginisialisasi konsep MVVM pada RegisterActivity
     * RegisterViewModel berfungsi untuk membuat logic register.
     * registeractivitybinding berfungsi untuk view MVVM di Register MVVM
     */
    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new RegisterViewModel(this);
        activityRegisterBinding.setRegisterview(registerViewModel);


        /**
         * memanggil presenter yang telat dibuat di RegisterView
         *  kemudian memanggil method yang telah di buat di RegisterViewModel yang beranama "sendRegisterRequest"
         */
        activityRegisterBinding.setPresenter(new RegisterView() {
            @Override
            public void RegisterData() {


                FormEditText fdtUsername = (FormEditText) findViewById(R.id.usernameku);
                FormEditText fdtEmail = (FormEditText) findViewById(R.id.emailku);
                FormEditText fdtPassword = (FormEditText) findViewById(R.id.passwordku);
                FormEditText fdtConfirmPassword = (FormEditText) findViewById(R.id.conpassword);
                FormEditText fdtPhone = (FormEditText) findViewById(R.id.phone);

                FormEditText[] allFields= {fdtUsername, fdtEmail, fdtPassword, fdtConfirmPassword, fdtPhone};
                boolean allValid = true;
                for (FormEditText field : allFields) {
                    allValid = field.testValidity() && allValid;
                }

                if (allValid) {
                    final String username = activityRegisterBinding.usernameku.getText().toString();
                    final String email = activityRegisterBinding.emailku.getText().toString();
                    final String phone = activityRegisterBinding.phone.getText().toString();
                    final String pasword = activityRegisterBinding.passwordku.getText().toString();

                    registerViewModel.sendRegister(username, email, phone, pasword);
                }
            }

            @Override
            public void IntentLogin() {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

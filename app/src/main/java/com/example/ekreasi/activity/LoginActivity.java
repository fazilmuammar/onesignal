package com.example.ekreasi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.EmailValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.widget.FormEditText;
import com.example.ekreasi.R;
import com.example.ekreasi.databinding.ActivityLoginBinding;
import com.example.ekreasi.dialog.ForgotPasswordDialog;
import com.example.ekreasi.view.LoginView;
import com.example.ekreasi.viewmodel.LoginViewModel;


/**
 * Activity for LOGIN layout resources
 *
 * Activity ini untuk user melakukan login dengan input email & password
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * variabel yang berfungsi untuk menginisialisasi konsep MVVM pada LoginActivity
     * LoginViewModel berfungsi untuk membuat logic coding.
     * loginactivitybinding berfungsi untuk view MVVM di LoginActivity
     */
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding loginActivittyBinding;

    /**
     * variabel intro yang berfungsi untuk membuat activity pengenalan aplikasi saat aplikasi pertama kali di install
     */
    int sess_intro;
    /**
     * variabel untuk sharedPreferences yang berfungsi untuk menyimpan data user (session) ketika user login
     * sehingga ketika aplikasi di destroy user tidak perlu melakukan login lagi.
     */
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            loginActivittyBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
            loginViewModel = new LoginViewModel(this);
            loginActivittyBinding.setLoginview(loginViewModel);

            loginActivittyBinding.forget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ForgotPasswordDialog().showForgetDialog(LoginActivity.this);
                }
            });

            /**
             * method untuk validator format email
             */

            FormEditText fdt = (FormEditText) findViewById(R.id.et_email);

            fdt.addValidator(
                new OrValidator(
                    "This Email is Invalid",
                    new EmailValidator(null) // same here for null
                )
            );


            /**
             * method untuk membuat Intro dan sharedPreference
             * sharedPreference disini mempunyai key bernama 'ekreasii'
             */
            sess_intro = 0;
            sharedPreferences = getSharedPreferences("ekreasii", Context.MODE_PRIVATE);
            sess_intro = sharedPreferences.getInt("ekreasii", 0);
            if (sess_intro == 0) {
                // set session
                sharedPreferences = getSharedPreferences("ekreasii", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putInt("ekreasii", 1);
                editor.apply();
                // call intro
                startActivity(new Intent(LoginActivity.this, IntroActivity.class));
                finish();
            }


            /**
             * memanggil presenter yang telat dibuat di LoginView
             *  kemudian memanggil method yang telah di buat di LoginViewModel yang beranama "sendLoginRequest"
             */
            loginActivittyBinding.setPresenter(new LoginView() {
                @Override
                public void LoginData() {
                    FormEditText fdtEmail = (FormEditText) findViewById(R.id.et_email);
                    FormEditText fdtPassword = (FormEditText) findViewById(R.id.password);

                    final String name = loginActivittyBinding.etEmail.getText().toString();
                    final String pass = loginActivittyBinding.password.getText().toString();

                    FormEditText[] allFields = {fdtEmail, fdtPassword};
                    boolean allValid = true;
                    for (FormEditText field : allFields) {
                        allValid = field.testValidity() && allValid;
                    }

                    if (allValid) {
                        loginViewModel.sendLoginRequest(name, pass);
                    }
                }

                @Override
                public void IntentRegister() {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).cancel();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

package com.example.ekreasi.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import com.example.ekreasi.activity.OTPView;
import com.example.ekreasi.helper.SessionManager;
import com.example.ekreasi.model.ResponseLogin;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity for LoginViewModel layout resources
 *
 * Class ini berfungsi untuk membuat Logic fungsi Login
 *
 * Library yang digunakan untuk connect ke server menggunakan Retrofit
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */

public class LoginViewModel extends Observable {

    private Context context;
    private SessionManager manager;

   public ObservableInt progressbar;

    public LoginViewModel(Context context) {
        this.context = context;
        progressbar = new ObservableInt(View.GONE);
    }


    /**
     * method yang digunakan untuk membuat fungsi  Login connect ke server menggunakan retrofit
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseLogin" yang terdapat di RestApi
     */
    public void sendLoginRequest(final String username, final String password) {
        progressbar.set(View.VISIBLE);

        RestApi api = InitRetrofit.getInstance();
        Call<ResponseLogin> loginCall = api.loginuser(
                username,
                password
        );
        loginCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                progressbar.set(View.GONE);
                if (response.isSuccessful()) {
                    String result = response.body().getResponse();

                    if (username.equals("") || password.equals("")) {
                        Toast.makeText(context, "Silahkan isi username & password anda", Toast.LENGTH_SHORT).show();

                    } else if (result.equals("success")) {
                        String user_id = response.body().getUserId();
                        String username = response.body().getUsername();
                        String email = response.body().getEmail();
                        String phone= response.body().getPhone();
                        String password = response.body().getPassword();

                        manager = new SessionManager(context);
                        manager.createLoginSession(user_id,username,email,phone,password);
                        manager.setUser_id(user_id);

                        context.startActivity(new Intent(context, OTPView.class));
                        ((Activity) context).finish();

                        Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("failed")) {
                        Toast.makeText(context, "Username / Password Salah", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cek Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {

            }
        });
    }
}

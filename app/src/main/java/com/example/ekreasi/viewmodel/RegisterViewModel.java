package com.example.ekreasi.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.ekreasi.activity.LoginActivity;
import com.example.ekreasi.model.ResponseRegister;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity for RegisterViewModel layout resources
 *
 * Class ini berfungsi untuk membuat Logic fungsi Register
 *
 * Library yang digunakan untuk connect ke server menggunakan Retrofit
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */

public class RegisterViewModel extends Observable {

    private Context context;

    public RegisterViewModel(Context context) {
        this.context = context;
    }


    /**
     * method yang digunakan untuk membuat fungsi Register connect ke server menggunakan retrofit
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseRegister" yang terdapat di RestApi
     */

    public void sendRegister(String username, String email, String phone, String password) {
        RestApi api = InitRetrofit.getInstance();
        Call<ResponseRegister> registerCall = api.registeruser(
            username,
            email,
            phone,
            password
        );

        registerCall.enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResponse();

                    if (result.equals("success")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(context, LoginActivity.class);
                                context.startActivity(i);
                                ((Activity) context).finish();
                            }
                        }, 4000);
                        Toast.makeText(context, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Pendaftaran gagal", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {

            }
        });
    }

}

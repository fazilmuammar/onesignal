package com.example.ekreasi.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.ekreasi.activity.EditArticleActivity;
import com.example.ekreasi.activity.HomeActivity;
import com.example.ekreasi.helper.SessionManager;
import com.example.ekreasi.model.ResponseEdit;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Activity for EditArticleViewModel layout resources
 *
 * Class ini berfungsi untuk membuat Logic fungsi Edit Article
 *
 * Library yang digunakan untuk connect ke server menggunakan Retrofit
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */


public class EditArticleViewModel extends Observable {
    private Context context;
    private SessionManager manager;


    public EditArticleViewModel(Context context) {
        this.context = context;

    }

    /**
     * method yang digunakan untuk membuat fungsi  Edit Aerticle connect ke server menggunakan retrofit
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseEdit" yang terdapat di RestApi
     */

    public void sendEditRequest(final String author,  final String title,  final String category, final String content, final String image, final String tanggal, final String phone,final String signatures) {
        final ProgressDialog loading = ProgressDialog.show(context, "Wait", "Loading");
        manager = new SessionManager(context);
        int user_id = Integer.parseInt(manager.getUser_id());
        int content_id = Integer.parseInt(EditArticleActivity.content_id);
        RestApi api = InitRetrofit.getInstance();
        Call<ResponseEdit> editCall = api.editarticle(
                user_id,
                content_id,
                author,
                title,
                category,
                content,
                image,
                tanggal,
                phone,
                signatures
        );

        editCall.enqueue(new Callback<ResponseEdit>() {
            @Override
            public void onResponse(Call<ResponseEdit> call, Response<ResponseEdit> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResponse();
                    if (result.equals("success")) {
                        Toast.makeText(context, "Article Berhasil Di Rubah", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context,HomeActivity.class));
                        ((Activity)context).finish();
                        loading.dismiss();
                    }



                } else{
                    Toast.makeText(context, "Datanya belum masuk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEdit> call, Throwable t) {

                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }
}

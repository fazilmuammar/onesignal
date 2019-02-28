package com.example.ekreasi.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.ekreasi.activity.HomeActivity;
import com.example.ekreasi.helper.SessionManager;
import com.example.ekreasi.model.ResponseAddArticle;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity for AddArticleViewModel layout resources
 *
 * Class ini berfungsi untuk membuat Logic fungsi Add Article
 *
 * Library yang digunakan untuk connect ke server menggunakan Retrofit
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */


public class AddArticleViewModel extends Observable {
    private Context context;
    private SessionManager manager;

    public AddArticleViewModel(Context context) {
        this.context = context;
    }

    /**
     * method yang digunakan untuk membuat fungsi  Add Aerticle connect ke server menggunakan retrofit
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseAddArticle" yang terdapat di RestApi
     */

    public void sendSaveRequest(final String username, final String title,  final String category, final String content,
                                final String image, final String tanggal, final String phone, final String signature)  {
        try {
            final ProgressDialog loading = ProgressDialog.show(context, "Wait", "Loading");
            manager = new SessionManager(context);
            int user_id = Integer.parseInt(manager.getUser_id());
            RestApi api = InitRetrofit.getInstance();
            Call<ResponseAddArticle> saveCall = api.addarticle(
                    user_id,
                    username,
                    title,
                    category,
                    content,
                    image,
                    tanggal,
                    phone,
                    signature
            );
            saveCall.enqueue(new Callback<ResponseAddArticle>() {
                @Override
                public void onResponse(Call<ResponseAddArticle> call, Response<ResponseAddArticle> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Article Berhasil Di Simpan", Toast.LENGTH_SHORT).show();

                        context.startActivity(new Intent(context,HomeActivity.class));
                        ((Activity)context).finish();

                        loading.dismiss();



                    } else if (response.equals("failed")) {
                        Toast.makeText(context, "Article gagal Di Simpan", Toast.LENGTH_SHORT).show();

                    }


                }


                @Override
                public void onFailure(Call<ResponseAddArticle> call, Throwable t) {
                    Toast.makeText(context, "Masalah Koneksi", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
        }

    }


}

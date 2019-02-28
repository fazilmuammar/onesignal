package com.example.ekreasi.network;

import com.example.ekreasi.model.ResponseAddArticle;
import com.example.ekreasi.model.ResponseArticleDetail;
import com.example.ekreasi.model.ResponseDelete;
import com.example.ekreasi.model.ResponseEdit;
import com.example.ekreasi.model.ResponseListArticle;
import com.example.ekreasi.model.ResponseLogin;
import com.example.ekreasi.model.ResponseRegister;
import com.example.ekreasi.model.ResponseSearch;
import com.example.ekreasi.model.ResultSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseRegister> registeruser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseLogin> loginuser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("create_content.php")
    Call<ResponseAddArticle> addarticle(
            @Field("user_id") int user_id,
            @Field("author") String author,
            @Field("title") String title,
            @Field("category_id") String category_id,
            @Field("content") String content,
            @Field("image") String image,
            @Field("tanggal") String tanggal,
            @Field("phone") String phone,
            @Field("signature") String signature
    );



    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseDelete> deleteArticle(
            @Field("content_id") String content_id

    );

    @FormUrlEncoded
    @POST("edit_article.php")
    Call<ResponseEdit> editarticle(
            @Field("user_id") int user_id,
            @Field("content_id") int content_id,
            @Field("author") String author,
            @Field("title") String title,
            @Field("category_id") String category_id,
            @Field("content") String content,
            @Field("image") String image,
            @Field("tanggal") String tanggal,
            @Field("phone") String phone,
            @Field("signature") String signature
    );



    @FormUrlEncoded
    @POST("read_article.php")
    Call<ResponseArticleDetail> readArticle(
            @Field("content_id") String content_id);


    @FormUrlEncoded
    @POST("list_search.php")
    Call<ResponseSearch> searchArticle(
            @Field("title") String Title);

    @GET("list_article.php")
    Call<ResponseListArticle> getRequestList();




}

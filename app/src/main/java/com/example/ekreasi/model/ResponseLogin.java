package com.example.ekreasi.model;

import com.google.gson.annotations.SerializedName;

import java.util.Observable;

public class ResponseLogin {

    @SerializedName("password")
    private String password;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("phone")
    private String phone;

    @SerializedName("response")
    private String response;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("email")
    private String email;

    @SerializedName("picture")
    private String picture;

    @SerializedName("username")
    private String username;

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public String getResponse(){
        return response;
    }

    public void setCreated(String created){
        this.created = created;
    }

    public String getCreated(){
        return created;
    }

    public void setUpdated(String updated){
        this.updated = updated;
    }

    public String getUpdated(){
        return updated;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public String getPicture(){
        return picture;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
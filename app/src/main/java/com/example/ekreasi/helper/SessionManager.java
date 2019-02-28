package com.example.ekreasi.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    private static final String KEY_TOKEN = "ekreasilogins";
    private static final String KEY_LOGIN = "ekreasii";
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public static final String SESS_ID       = "user_id";
    public static final String SESS_NAME     = "username";
    public static final String SESS_EMAIL    = "email";
    public static final String SESS_PASS     = "password";
    public static final String SESS_PHONE    = "phone";
    public static final String SESS_PICTURE  = "picture";

    int PRIVATE_MODE = 0;
    Context c;

    //0 agar cuma bsa dibaca hp itu sendiri
    String PREF_NAME = "TransfoodPreffs";

    //konstruktor


    public SessionManager(Context c) {
        this.c = c;
        pref = c.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<>();
        user.put(SESS_ID,    pref.getString(SESS_ID,null));
        user.put(SESS_NAME,  pref.getString(SESS_NAME,null));
        user.put(SESS_EMAIL, pref.getString(SESS_EMAIL,null));
        user.put(SESS_PASS,  pref.getString(SESS_PASS,null));
        user.put(SESS_PHONE,  pref.getString(SESS_PHONE,null));
        user.put(SESS_PICTURE,  pref.getString(SESS_PICTURE,null));

            return user;
    }


    //membuat session login
    public void createLoginSession(String user_id,String username,String email, String phone,String password) {
        editor.putString(KEY_TOKEN, user_id);
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(SESS_NAME,username);
        editor.putString(SESS_EMAIL,email);
        editor.putString(SESS_PHONE,phone);
        editor.putString(SESS_PASS,password);
        editor.commit();
        //commit digunakan untuk menyimpan perubahan
    }

    //mendapatkan token
    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    //cek login
    public boolean isLogin() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    //logout user
    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void setUser_id(String user_id) {
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public String getUser_id() {
        return pref.getString("user_id", "");
    }

    public void setUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString("username", "");
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString("email", "");
    }

    public void setPhone(String phone) {
        editor.putString("phone", phone);
        editor.commit();
    }

    public String getPhone() {
        return pref.getString("phone", "");
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString("password", "");
    }

    public void setPicture(String picture) {
        editor.putString("picture", picture);
        editor.commit();
    }

    public String getPicture() {
        return pref.getString("picture", "");
    }

    public void setUserTenant(String user_tenant) {

    }




}

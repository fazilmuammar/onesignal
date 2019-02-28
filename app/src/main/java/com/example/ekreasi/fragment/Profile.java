package com.example.ekreasi.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekreasi.R;
import com.example.ekreasi.helper.SessionManager;
import com.example.ekreasi.model.ResponseLogin;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public class Profile extends Fragment {

    private Context context;

    CircleImageView fotoprofile;
    TextView myusername,myemail,myphone,mypassword;


    HashMap<String,String> sessPref;

    SessionManager manager;

    public Profile(Context context) {
        // Required empty public constructor
        this.context = context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View myFragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_profile, null, false);

        fotoprofile = myFragmentView.findViewById(R.id.gambarku);
        myusername = myFragmentView.findViewById(R.id.myuserrnames);
        myemail = myFragmentView.findViewById(R.id.myemails);
        myphone = myFragmentView.findViewById(R.id.myphones);
        mypassword = myFragmentView.findViewById(R.id.myapassword);

        manager = new SessionManager(context);

        sessPref = manager.getUserDetails();



       myusername.setText(sessPref.get(manager.SESS_NAME));

        myemail.setText(sessPref.get(manager.SESS_EMAIL));

        myphone.setText(sessPref.get(manager.SESS_PHONE));

        mypassword.setText(sessPref.get(manager.SESS_PASS));






        return myFragmentView;
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }



}

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
import android.widget.ImageView;

import com.example.ekreasi.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class popup_dialog extends Fragment {

    Context myContext;
    Button popUp;
    Button dialogPop;

    public popup_dialog(Context context) {
        // Required empty public constructor
        this.myContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_popup_dialog, container, false);
        View myFragmentView =LayoutInflater.from(myContext).inflate(R.layout.fragment_popup_dialog, null, false);
        // Inflate the layout for this fragment
        return myFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popUp = (Button)view.findViewById(R.id.popUp);
        dialogPop = (Button)view.findViewById(R.id.dialog_pop);
        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(myContext, R.style.CustomDialogTheme)
                        .setTitle("Nuke planet Jupiter?")
                        .setMessage("Note that nuking planet Jupiter will destroy everything in there.")
                        .setPositiveButton("Nuke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                            }
                        })
                        .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Aborting mission...");
                            }
                        })
                        .show();
            }
        });

        dialogPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });



        getActivity().setTitle("Dialog");
    }

    public void showPopUp() {
        ImageView image = new ImageView(myContext);
        image.setImageResource(R.drawable.bg);

        new AlertDialog.Builder(myContext, R.style.popUpStyle)
                .setView(image)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .show();
    }



}

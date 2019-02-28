package com.example.ekreasi.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.example.ekreasi.R;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class webV extends Fragment {

    Context mContext;
    WebView web;
    Button goButton;
    EditText goText;


    public webV(Context context) {
        // Required empty public constructor
        this.mContext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =LayoutInflater.from(mContext).inflate(R.layout.fragment_web_v, null, false);
        return myFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        web = (WebView) view.findViewById(R.id.webV);
        goButton = (Button) view.findViewById(R.id.button_go);
        goText = (EditText) view.findViewById(R.id.text_go);


        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSurfer();
                hideKeyboard(v);

            }
        });

        goText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_SPACE)) {
                    //do code
                    goSurfer();
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        getActivity().setTitle("Web View");



    }

    public void goSurfer(){
        final String txtGo = "http://" + goText.getText().toString();
        try{
            web.setWebViewClient(new MyBrowser());
            web.loadUrl(txtGo);
            Log.d("Web", txtGo);
        }catch (Exception e) {
            StringWriter err = new StringWriter();
            e.printStackTrace(new PrintWriter(err));
            new AlertDialog.Builder(mContext, R.style.CustomDialogTheme)
                    .setTitle("Error")
                    .setMessage(err.toString())
                    .setPositiveButton("Oke Aja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("webView", "Oke Aja Pun...");
                        }
                    });
        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.web_menu, menu);
        Activity activity = getActivity();
        if(Build.VERSION.SDK_INT > 11) {
            activity.invalidateOptionsMenu();
            if(connectAvailable() == "true"){
                menu.findItem(R.id.app_bar_signal).setIcon(R.drawable.ic_signal_on);
            }else {
                menu.findItem(R.id.app_bar_signal).setIcon(R.drawable.ic_signal_off);
            }
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private String connectAvailable() {
        String connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = "true";
        } else {
            connected = "false";
        }

        return connected;
    }

}

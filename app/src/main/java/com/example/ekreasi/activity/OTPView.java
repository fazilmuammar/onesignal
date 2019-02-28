package com.example.ekreasi.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alahammad.otp_view.OTPListener;
import com.alahammad.otp_view.OtpView;
import com.alahammad.otp_view.smsCatcher.OnSmsCatchListener;
import com.alahammad.otp_view.smsCatcher.SmsVerifyCatcher;
import com.example.ekreasi.R;

public class OTPView extends AppCompatActivity implements OTPListener, OnSmsCatchListener<String> {

    OtpView otpView;
    SmsVerifyCatcher smsVerifyCatcher;


    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpview);

        verify = (Button) findViewById(R.id.verify);
        otpView = findViewById(R.id.otp);
        otpView.setOnOtpFinished(this);
        smsVerifyCatcher = new SmsVerifyCatcher(this, this);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpView.hasValidOTP()){
                    startActivity(new Intent(OTPView.this, HomeActivity.class));
                    finish();
                }  else{
                    Toast.makeText(OTPView.this, "Plaese Input Code OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void otpFinished(String otp) {

    }

    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onSmsCatch(String message) {

    }
}

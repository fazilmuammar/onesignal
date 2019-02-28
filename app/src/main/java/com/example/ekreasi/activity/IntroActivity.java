package com.example.ekreasi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.ekreasi.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("eKreasi", "PT. Integrasi Media Kreasi (eKreasi) is a Creative Digital Company established on the early 2011 with services.", R.drawable.aa, getResources().getColor(R.color.colorIntroOne)));
        addSlide(AppIntroFragment.newInstance("Writing", "Writing is a medium of human communication that represents language and emotion with signs and symbols.",
                R.drawable.b, getResources().getColor(R.color.colorIntroTwo)));
        addSlide(AppIntroFragment.newInstance("Sharing", "Sharing is the joint use of a resource or space. In its narrow sense, it refers to joint or alternating use of inherently finite.", R.drawable.c, getResources().getColor(R.color.colorIntroThree)));

        showSkipButton(true);
        setProgressButtonEnabled(true);

        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

    }
}


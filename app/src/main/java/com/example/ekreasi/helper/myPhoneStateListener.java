package com.example.ekreasi.helper;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;


public class myPhoneStateListener extends PhoneStateListener {

    public int signalStrengthValue;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if (signalStrength.isGsm()) {
            if (signalStrength.getGsmSignalStrength() != 99)
                signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
            else
                signalStrengthValue = signalStrength.getGsmSignalStrength();
        } else {
            signalStrengthValue = signalStrength.getCdmaDbm();
        }
//        txtSignalStr.setText("Signal Strength : " + signalStrengthValue);
        Log.d("myPhoneStateListener", "onSignalStrengthsChanged: " + signalStrengthValue );
    }
}
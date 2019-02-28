package com.example.ekreasi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.EmailValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.widget.FormEditText;
import com.example.ekreasi.R;

public class ForgotPasswordDialog {

    public void showForgetDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_forgotpassword, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        final FormEditText fdt = (FormEditText) view.findViewById(R.id.et_emails);
        fdt.addValidator(
                new OrValidator(
                        "Email address not valid",
                        new EmailValidator(null) // same here for null
                )
        );

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( fdt.testValidity() ) {
                    Toast.makeText(context, "Silahkan Cek Email Anda", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }


}

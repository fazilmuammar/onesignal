package com.example.ekreasi.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekreasi.R;
import com.example.ekreasi.helper.AnyOrientationCaptureActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;





@SuppressLint("ValidFragment")
public class QRCode extends Fragment {

    // TODO: Rename and change types of parameters
    Context mContext;
    Button generateButton;
    Button scanButton;
    ImageView qrCodeView;
    EditText textData;
    TextView txtResult;
    private IntentIntegrator qrScan;


    public QRCode(Context context) {
        // Required empty public constructor
        this.mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =LayoutInflater.from(mContext).inflate(R.layout.fragment_qrcode, null, false);
        return myFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateButton  = (Button) view.findViewById(R.id.button_generate);
        qrCodeView = (ImageView) view.findViewById(R.id.imageQR);
        textData = (EditText) view.findViewById(R.id.text_data);
        scanButton = (Button) view.findViewById(R.id.button_scan);
        txtResult = (TextView) view.findViewById(R.id.txt_resultScan);



        generateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                generateQR();
                hideKeyboard(v);
            }
        });



        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR();
            }
        });
        Bitmap bmBack = BitmapFactory.decodeResource(getResources(), R.drawable.aacopy);
        qrCodeView.setImageBitmap(bmBack);

        getActivity().setTitle("QR Code");


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void generateQR(){
        String textQR = textData.getText().toString();
        MultiFormatWriter multiFormatwriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatwriter.encode(textQR,BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.aekreasi);
            Bitmap bitmapMerging = mergeToPin(bitmap,bm);
            qrCodeView.setImageBitmap(bitmapMerging);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Bitmap mergeToPin(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, move, move, paint);
        return result;
    }

    public void scanQR(){
        //intializing scan object
        qrScan = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
//        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//        qrScan.setBarcodeImageEnabled(true);
        qrScan.setCaptureActivity(AnyOrientationCaptureActivity.class);
        qrScan.setOrientationLocked(false);
        qrScan.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                txtResult.setText(result.getContents());
            }
        } else {
//            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(getContext(),"No scan data received!", Toast.LENGTH_SHORT).show();

        }

    }



}

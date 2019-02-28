package com.example.ekreasi.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.EmailValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.widget.FormEditText;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.ekreasi.R;
import com.example.ekreasi.databinding.ActivityAddArticleBinding;
import com.example.ekreasi.utils.UtilityCamera;
import com.example.ekreasi.view.AddArticleView;
import com.example.ekreasi.viewmodel.AddArticleViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Activity for LOGIN layout resources
 *
 * Activity ini untuk user melakukan Add Article
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */
public class AddArticleActivity extends AppCompatActivity {


    /**
     * Variable Camera & Gallery
     */
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, CODE;
    private String userChoosenTask;
    Bitmap bitmap, mybitmap;

    /**
     * Variable Category
     */
    String CATEGORY_ID;

    /**
     * Variable Calender
     */
    final Calendar myCalendar = Calendar.getInstance();


    /**
     * Variable Signature
     */
    Button btn_get_sign, mClear, mGetSign, mCancel;
    ImageView img_sig;
    static String ConvertedBitmap;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    AddArticleActivity.signature mSignature;
    android.support.v7.app.AlertDialog.Builder builder;
    LinearLayout layout;
    private int STORAGE_PERMISSION_CODE = 1;

    /**
     * variabel yang berfungsi untuk menginisialisasi konsep MVVM pada Add Arcticle Activity
     * AddArticleViewModel berfungsi untuk membuat logic coding.
     * addArticlebinding berfungsi untuk view MVVM di AddArticleActivity
     */

    private AddArticleViewModel addArticleViewModel;
    private ActivityAddArticleBinding addArticleBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storagepermission();
        ButterKnife.bind(this);

        addArticleBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_article);
        addArticleViewModel = new AddArticleViewModel(this);
        addArticleBinding.setAddarticleview(addArticleViewModel);

        /**
         * Permission untuk Camera
         */
        if (ContextCompat.checkSelfPermission(AddArticleActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddArticleActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }



        /**
         * inisialisasi VARIABEL DAN PANGGIL METHOD SIGNATURE pada btn_get_sign
         */
        img_sig = (ImageView)findViewById(R.id.gambarttd);
        btn_get_sign = (Button)findViewById(R.id.getku);
        layout = (LinearLayout) findViewById(R.id.layout);

        dialog = new Dialog(AddArticleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        btn_get_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertedBitmap = "";
                dialog_action();
            }
        });


        /**
         * Inisiasi VARIABEL UNTUK SPINNER
         */

        CATEGORY_ID = "";

        //METHOD VALIDATOR EMAIL
        addArticleBinding.editName.addValidator(
                new OrValidator(
                        "This Email is Invalid",
                        new EmailValidator(null) // same here for null
                )
        );

        /**
         * Method untuk DatePicker
         */
        addArticleBinding.tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddArticleActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /**
         * Method untuk memilih gambar
         */
        addArticleBinding.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                Toast.makeText(AddArticleActivity.this, String.valueOf(CODE), Toast.LENGTH_LONG).show();
            }
        });


        //METHOD BACK
        addArticleBinding.backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddArticleActivity.this, HomeActivity.class));
                finish();
            }
        });

        /**
         * Panggil Method Spinner
         */
        CallSpinner();



        /**
         * memanggil presenter yang telat dibuat di AddArticleView
         *  kemudian memanggil method yang telah di buat di AddArticleViewModel yang beranama "sendSaveRequest"
         */
        addArticleBinding.setPresenter(new AddArticleView() {
            @Override
            public void SaveData() {

                if (CODE == 1) {
                    String images = getStringImage(bitmap);
                    String signature = imageToString(mybitmap);

                    final String username = addArticleBinding.editTitle.getText().toString();
                    final String title = addArticleBinding.editName.getText().toString();
                    final String category = CATEGORY_ID;
                    final String content = addArticleBinding.editContent.getText().toString();
                    final String phone = addArticleBinding.phone.getText().toString();
                    final String image = images;
                    final String signatures = signature;
                    final String tanggal = addArticleBinding.tanggal.getText().toString();



                    if (CODE == 0 && addArticleBinding.editName.getText().toString().equals("") || addArticleBinding.editTitle.getText().toString().equals("")
                            || addArticleBinding.editContent.getText().toString().equals("") || addArticleBinding.tanggal.getText().toString().equals("")
                            || addArticleBinding.phone.getText().toString().equals("") || addArticleBinding.gambarttd.getDrawable() == null) {

                        Toast.makeText(AddArticleActivity.this, "Isi Data yang Lengkap", Toast.LENGTH_SHORT).show();
                    } else if (addArticleBinding.phone.getText().toString().length() < 12) {
                        Toast.makeText(AddArticleActivity.this, "Phone Number is wrong", Toast.LENGTH_SHORT).show();

                    } else {
                        addArticleViewModel.sendSaveRequest(username, title, category, content, image, tanggal, phone,signatures);

                    }

                } else if (CODE == 0) {
                    Toast.makeText(AddArticleActivity.this, "Isi Data nya Lengkap", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void DisplayImage() {
                selectImage();
            }

        });
    }



    /**
     * Method untuk membuat FITUR SIGNATURE Dan convert image Signature ke String
     */

    public void dialog_action() {

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new AddArticleActivity.signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.v("log_tag", "Panel Cleared");

                mSignature.clear();
                mybitmap = null;
                addArticleBinding.gambarttd.setImageDrawable(null);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // Creating Separate Directory for saving Generated Images
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DCIM/CREAMLINE/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name+ ".png";
                //save to static string
                ConvertedBitmap = StoredPath;
                // Method to create Directory, if the Directory doesn't exists
                File file = new File(DIRECTORY);
                if (!file.exists()) {
                    file.mkdir();
                    /*Toast.makeText(getApplicationContext(), "Folder created", Toast.LENGTH_SHORT).show();*/
                    Snackbar snackbar = Snackbar.make(layout, "Folder created successfully!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

                view.setDrawingCacheEnabled(true);

                mSignature.save(view, StoredPath);
                dialog.dismiss();


                if (addArticleBinding.gambarttd.equals("")){

                    builder.setTitle("Reminder!");
                    builder.setMessage("Please make sure all required fields are not empty. Before getting the driver's Signature");
                    //builder.setIcon(R.drawable.ic_priority_high_black_24dp);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        }
                    });
                    builder.show();
                }

                else
                {
                    Snackbar snackbar = Snackbar.make(layout, "Signature saved successfully!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //selectImage1();
                    File imgFile = new  File(ConvertedBitmap);

                    if(imgFile.exists()){

                        mybitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = (ImageView) findViewById(R.id.gambarttd);

                        myImage.setImageBitmap(mybitmap);
                    }
                }

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                if (addArticleBinding.gambarttd.equals(null)){
                    mSignature.clear();
                    dialog.dismiss();
                }else{
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    static Canvas canvas;

    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            if (mybitmap == null) {
                mybitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            } else if(mybitmap != null) {
                mybitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }

            canvas = new Canvas(mybitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                mybitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);

                if (mybitmap  == null) {
                    Toast.makeText(AddArticleActivity.this, "Isi gambarnya dullu", Toast.LENGTH_SHORT).show();
                }
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {

            }

        }

        public void clear() {
            path.reset();
            invalidate();
            mContent.removeAllViews();
            mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
            mSignature = new AddArticleActivity.signature(getApplicationContext(), null);
            mSignature.setBackgroundColor(Color.WHITE);
            mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }


    }


    private String imageToString(Bitmap mybitmap){
            String result = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mybitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            result = Base64.encodeToString(imgBytes,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
            return result;
    }

    public void Storagepermission(){

        if (ContextCompat.checkSelfPermission(AddArticleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

        }
        else {

            requestStoragePermission();
        }

    }

    public void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new android.support.v7.app.AlertDialog.Builder(this).setTitle("Storage Permission needed!").setMessage("This permission is needed")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(AddArticleActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);


                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();



        }else {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }




    /**
     * Method untuk Membuat Spinner
     */
    public void CallSpinner() {
        AndroidNetworking.post("http://fazilmuammar007.com/blogapp/list_category.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        try {

                            ArrayList<String> category_id = new ArrayList<String>();
                            ArrayList<String> name = new ArrayList<String>();
                            category_id.add("");
                            name.add("Pilih Kategori");

                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                category_id.add(jsonObject.optString("category_id"));
                                name.add(jsonObject.optString("name"));
                            }


                            ArrayAdapter arrayAdapter;
                            arrayAdapter = new ArrayAdapter<String>(AddArticleActivity.this, R.layout.list_spinner, category_id);
                            addArticleBinding.spinCategoryId.setAdapter(arrayAdapter);
                            arrayAdapter = new ArrayAdapter<String>(AddArticleActivity.this, R.layout.list_spinner, name);
                            addArticleBinding.spinCategory.setAdapter(arrayAdapter);

                            // mengambil id kategori ketika mengklik spinner
                            addArticleBinding.spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String idd = addArticleBinding.spinCategoryId.getItemAtPosition(position).toString();
                                    CATEGORY_ID = addArticleBinding.spinCategoryId.getItemAtPosition(position).toString();
                                    Log.d("category_id", idd);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }




    /**
     * Method untuk membuat FITUR Intent ke Galery dan Camera
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case UtilityCamera.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else if (requestCode == STORAGE_PERMISSION_CODE) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(MainActivity.this, "storage permission is granted", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(AddArticleActivity.this, "storage permission is denied", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddArticleActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityCamera.checkPermission(AddArticleActivity.this);

                if (items[item].equals("Take Photo")) {
                    CODE = 1;
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    CODE = 1;
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    CODE = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                addArticleBinding.gambar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addArticleBinding.gambar.setImageBitmap(thumbnail);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addArticleBinding.gambar.setImageBitmap(bm);
    }





    /**
     * Method untuk DatePicker yang akan di panggil di EditText tanggal
     */
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        addArticleBinding.tanggal.setText(sdf.format(myCalendar.getTime()));
    }




}
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
import android.support.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ekreasi.R;
import com.example.ekreasi.databinding.ActivityEditBinding;
import com.example.ekreasi.utils.UtilityCamera;
import com.example.ekreasi.view.EditArticleView;
import com.example.ekreasi.viewmodel.EditArticleViewModel;

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
import butterknife.OnClick;

public class EditArticleActivity extends AppCompatActivity {

    /**
     * Variable Category
     */
    String CATEGORY_ID;


    /**
     * Variable Camera & Gallery
     */
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, CODE;
    private String userChoosenTask;
    Bitmap bitmap;


    /**
     * Variable untuk Put Extra dari Class CustomRecyleradapter
     */
    public static String email, author, category, tanggals, phones, content, gambars, content_id, categorys;

    Context mContext;


    /**
     * Variable Signature
     */
    Button btn_get_sign, mClear, mGetSign, mCancel;
    ImageView img_sig;
    static String ConvertedBitmap;
    Dialog dialog;
    LinearLayout layout;
    View view;
    signature mSignature;
    android.support.v7.app.AlertDialog.Builder builder;
    private int STORAGE_PERMISSION_CODE = 1;

    /**
     * variabel yang berfungsi untuk menginisialisasi konsep MVVM pada Edit Arcticle Activity
     * EditArticleViewModel berfungsi untuk membuat logic coding.
     * editArticlebinding berfungsi untuk view MVVM di EditArticle Activity
     */

    private EditArticleViewModel editArticleViewModel;
    private ActivityEditBinding editArticleBinding;
    private Bitmap bmp, mybitmaps;

    /**
     * Variable Calender
     */
    final Calendar myCalendar = Calendar.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storagepermission();
        setContentView(R.layout.activity_edit);
        editArticleBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        editArticleViewModel = new EditArticleViewModel(this);
        editArticleBinding.setEditarticleview(editArticleViewModel);
        ButterKnife.bind(this);





        /**
         * inisialisasi VARIABEL DAN PANGGIL METHOD SIGNATURE pada btn_get_sign
         */
        img_sig = (ImageView) findViewById(R.id.gambarttd);
        btn_get_sign = (Button) findViewById(R.id.getku);
        layout = (LinearLayout) findViewById(R.id.layout);

        dialog = new Dialog(EditArticleActivity.this);
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
         * Method untuk Validasi
         */
        FormEditText editName = (FormEditText) findViewById(R.id.edit_name);
        editName.addValidator(
                new OrValidator(
                        "This Email is Invalid",
                        new EmailValidator(null) // same here for null
                )
        );


        /**
         * Method untuk DatePicker
         */

        editArticleBinding.tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditArticleActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /**
         * Method untuk memilih gambar
         */
        editArticleBinding.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CODE = 1;
                selectImage();

            }
        });

        /**
         * Inisiasi VARIABEL UNTUK SPINNER
         */
        CATEGORY_ID = "";

        /**
         * Panggil Method Spinner
         */
        CallSpinner();


        /**
         *Inisiasi Variabel put extra untuk memilih article yang akan di edit dari CustomRecyclerAdapter
         */
        email = this.getExtras("email");
        author = this.getExtras("author");
        category = this.getExtras("category");
        tanggals = this.getExtras("tanggal");
        phones = this.getExtras("phone");
        content = this.getExtras("content");
        gambars = this.getExtras("gambar");
        categorys = this.getExtras("category");


        this.SetTexting();

        ParseIntentFromLink();


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with((Activity) EditArticleActivity.this).asBitmap()
                        .load(getIntent().getStringExtra("gambar"))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                editArticleBinding.gambar.setImageBitmap(resource);
                                bmp = resource;

                            }
                        });

            }
        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with((Activity) EditArticleActivity.this).asBitmap()
                        .load(getIntent().getStringExtra("signature"))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                editArticleBinding.gambarttd.setImageBitmap(resource);
                                mybitmaps = resource;

                            }
                        });

            }
        });

    }


    /**
     *METHOD UNTUK AMBIL DATA PUT EXTRA DARI LIST ARTICLE YANG AKAN DIEDIT
     */

    private String getExtras(String values) {
        return getIntent().getStringExtra(values);
    }

    private void SetTexting() {
        editArticleBinding.editName.setText(email.equals("") ? "-" : email);
        editArticleBinding.editTitle.setText(author.equals("") ? "-" : author);
        editArticleBinding.tanggal.setText(tanggals.equals("") ? "-" : tanggals);
        editArticleBinding.phone.setText(phones.equals("") ? "-" : phones);
        editArticleBinding.editContent.setText(content.equals("") ? "-" : content);
    }

    private void ParseIntentFromLink() {
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null) {
            email = uri.getQueryParameter("email");
            author = uri.getQueryParameter("author");
            category = uri.getQueryParameter("category");
            tanggals = uri.getQueryParameter("tanggal");
            phones = uri.getQueryParameter("phone");
            content = uri.getQueryParameter("content");
            categorys = uri.getQueryParameter("category");
            content_id = uri.getQueryParameter("content_id");

        } else {
            email = getIntent().getStringExtra("email");
            author = getIntent().getStringExtra("author");
            category = getIntent().getStringExtra("category");
            tanggals = getIntent().getStringExtra("tanggal");
            phones = getIntent().getStringExtra("phone");
            content = getIntent().getStringExtra("content");
            categorys = getIntent().getStringExtra("category");
            content_id = getIntent().getStringExtra("content_id");
        }

        Edit();

    }




    /**
     * memanggil presenter yang telat dibuat di EditArticleView
     *  kemudian memanggil method yang telah di buat di EditArticleViewModel yang beranama "sendEditRequest"
     */
    private void Edit() {
        editArticleBinding.setPresenter(new EditArticleView() {
            @Override
            public void EditData() {

                if (CODE == 1) {
                    String myimages = getStringImage(bitmap);
                    String signatures = imageToString(mybitmaps);
                    final String author = editArticleBinding.editTitle.getText().toString();
                    final String title = editArticleBinding.editName.getText().toString();
                    final String category = CATEGORY_ID;
                    final String content = editArticleBinding.editContent.getText().toString();
                    final String phone = editArticleBinding.phone.getText().toString();
                    final String image = myimages;
                    final String signature = signatures;


                    final String tanggal = editArticleBinding.tanggal.getText().toString();

                    if (CODE == 0 && editArticleBinding.editName.getText().toString().equals("") || editArticleBinding.editName.getText().toString().equals("") || editArticleBinding.editTitle.getText().toString().equals("")
                            || editArticleBinding.editContent.getText().toString().equals("") || editArticleBinding.tanggal.getText().toString().equals("") || editArticleBinding.phone.getText().toString().equals("")) {
                        Toast.makeText(EditArticleActivity.this, "Isi Data yang Lengkap", Toast.LENGTH_SHORT).show();
                    } else if (editArticleBinding.phone.getText().toString().length() < 12) {
                        Toast.makeText(EditArticleActivity.this, "Phone Number is wrong", Toast.LENGTH_SHORT).show();
                    } else if (editArticleBinding.spinCategoryId.getSelectedItem().toString().equals(String.valueOf(category))) {
                        Toast.makeText(EditArticleActivity.this, "Isi Kategory ", Toast.LENGTH_SHORT).show();
                    } else {
                        editArticleViewModel.sendEditRequest(author, title, category, content, image, tanggal, phone,signature);
                    }

                } else if (CODE == 0) {
                    String myimages = getStringImage(bmp);
                    String signatures = imageToString(mybitmaps);

                    final String image = myimages;
                    final String signature = signatures;
                    final String author = editArticleBinding.editTitle.getText().toString();
                    final String title = editArticleBinding.editName.getText().toString();
                    final String category = CATEGORY_ID;
                    final String content = editArticleBinding.editContent.getText().toString();
                    final String phones = editArticleBinding.phone.getText().toString();
                    final String tanggals = editArticleBinding.tanggal.getText().toString();

                    if (editArticleBinding.editName.getText().toString().equals("") || editArticleBinding.editName.getText().toString().equals("") || editArticleBinding.editTitle.getText().toString().equals("")
                            || editArticleBinding.editContent.getText().toString().equals("") || editArticleBinding.tanggal.getText().toString().equals("")
                            || editArticleBinding.phone.getText().toString().equals("")) {
                        Toast.makeText(EditArticleActivity.this, "Isi Data yang Lengkap", Toast.LENGTH_SHORT).show();
                    } else if (editArticleBinding.phone.getText().toString().length() < 12) {
                        Toast.makeText(EditArticleActivity.this, "Phone Number is wrong", Toast.LENGTH_SHORT).show();
                    } else if (editArticleBinding.spinCategoryId.getSelectedItem().toString().equals(String.valueOf(category))) {
                        Toast.makeText(EditArticleActivity.this, "Isi Kategory ", Toast.LENGTH_SHORT).show();
                    } else {
                        editArticleViewModel.sendEditRequest(author, title, category, content, image, tanggals, phones,signature);
                    }
                }

            }

            @Override
            public void DisplayImage() {
                selectImage();
            }
        });
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
                        try {

                            ArrayList<String> category_id = new ArrayList<String>();
                            ArrayList<String> name = new ArrayList<String>();
                            category_id.add("");
                            name.add("Pilih Kategory");

                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                category_id.add(jsonObject.optString("category_id"));
                                name.add(jsonObject.optString("name"));
                            }


                            ArrayAdapter arrayAdapter;
                            arrayAdapter = new ArrayAdapter<String>(EditArticleActivity.this, R.layout.list_spinner, category_id);
                            editArticleBinding.spinCategoryId.setAdapter(arrayAdapter);
                            arrayAdapter = new ArrayAdapter<String>(EditArticleActivity.this, R.layout.list_spinner, name);
                            editArticleBinding.spinCategory.setAdapter(arrayAdapter);

                            // mengambil id kategori ketika mengklik spinner
                            editArticleBinding.spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String idd = editArticleBinding.spinCategoryId.getItemAtPosition(position).toString();
                                    CATEGORY_ID = editArticleBinding.spinCategoryId.getItemAtPosition(position).toString();

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
                } else {

                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditArticleActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityCamera.checkPermission(EditArticleActivity.this);

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
                editArticleBinding.gambar.setImageBitmap(bitmap);
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

        editArticleBinding.gambar.setImageBitmap(thumbnail);


    }


    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        editArticleBinding.gambar.setImageBitmap(bm);
    }


    /**
     * Method untuk membuat FITUR SIGNATURE Dan convert image Signature ke String
     */

    public void dialog_action(){
        layout = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);

        layout.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = layout;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSignature.clear();
                mybitmaps = null;
                editArticleBinding.gambarttd.setImageDrawable(null);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DCIM/CREAMLINE/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name + ".png";
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


                if (editArticleBinding.gambarttd.equals("")) {

                    builder.setTitle("Reminder!");
                    builder.setMessage("Please make sure all required fields are not empty. Before getting the driver's Signature");
                    //builder.setIcon(R.drawable.ic_priority_high_black_24dp);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    builder.show();
                } else {
                    Snackbar snackbar = Snackbar.make(layout, "Signature saved successfully!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //selectImage1();
                    File imgFile = new File(ConvertedBitmap);

                    if (imgFile.exists()) {

                        mybitmaps = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = (ImageView) findViewById(R.id.gambarttd);

                        myImage.setImageBitmap(mybitmaps);
                    }
                }

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                if (editArticleBinding.gambarttd.equals(null)) {
                    mSignature.clear();
                    dialog.dismiss();
                } else {
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
            if (mybitmaps == null) {
                mybitmaps = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.RGB_565);
            } else if (mybitmaps != null) {
                mybitmaps = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.RGB_565);
            }

            canvas = new Canvas(mybitmaps);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                mybitmaps.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {

            }

        }

        public void clear() {
            path.reset();
            invalidate();
            layout.removeAllViews();
            layout = (LinearLayout) dialog.findViewById(R.id.linearLayout);
            mSignature = new signature(getApplicationContext(), null);
            mSignature.setBackgroundColor(Color.WHITE);
            layout.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    private String imageToString(Bitmap mybitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mybitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        Bitmap emptyBitmap = Bitmap.createBitmap(mybitmap.getWidth(), mybitmap.getHeight(), mybitmap.getConfig());
        if (mybitmap.sameAs(emptyBitmap)) {
            Toast.makeText(mContext, "sasasasasa", Toast.LENGTH_SHORT).show();
        }

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);

    }

    public void Storagepermission() {

        if (ContextCompat.checkSelfPermission(EditArticleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {

            requestStoragePermission();
        }

    }

    public void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new android.support.v7.app.AlertDialog.Builder(this).setTitle("Storage Permission needed!").setMessage("This permission is needed")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(EditArticleActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);


                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();


        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }



    @OnClick(R.id.back)
    public void onViewClicked() {
        startActivity(new Intent(EditArticleActivity.this, HomeActivity.class));
        finish();
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
        editArticleBinding.tanggal.setText(sdf.format(myCalendar.getTime()));
    }



}

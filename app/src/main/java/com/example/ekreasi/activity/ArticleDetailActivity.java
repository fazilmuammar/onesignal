package com.example.ekreasi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.example.ekreasi.R;
import com.example.ekreasi.model.ResponseArticleDetail;
import com.example.ekreasi.model.ResultArticleDetail;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleDetailActivity extends AppCompatActivity {


    @BindView(R.id.my_toolbar)
    TextView myToolbar;
    @BindView(R.id.backs)
    ImageButton backs;
    @BindView(R.id.share)
    ImageButton share;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_content_id)
    TextView textContentId;
    @BindView(R.id.text_category_id)
    TextView textCategoryId;
    @BindView(R.id.text_categor)
    TextView textCategor;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.text_username)
    TextView textUsername;
    @BindView(R.id.text_created)
    TextView textCreated;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.ttd)
    ImageView ttd;


    private List<ResultArticleDetail> dataarticle;

    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // BUAT INTENT READ ARTICLE
        bundle = getIntent().getExtras();

        //PANGGIL METHOD READ ARTICLE
        ReadArticle();
    }


    // METHOD BUAT READ ARTICLE
    private void ReadArticle() {
        final ProgressDialog loading = ProgressDialog.show(this, "Wait", "Loading");
        RestApi restApi = InitRetrofit.getInstance();
        final Call<ResponseArticleDetail> articleDetailCall = restApi.readArticle(bundle.getString("content_id"));
        articleDetailCall.enqueue(new Callback<ResponseArticleDetail>() {
            @Override
            public void onResponse(Call<ResponseArticleDetail> call, Response<ResponseArticleDetail> response) {
                List<ResultArticleDetail> result = response.body().getResult1();

                textTitle.setText(result.get(0).getTitle());
                textUsername.setText(result.get(0).getAuthor());
                content.setText(result.get(0).getContent());
                textCreated.setText(result.get(0).getTanggal());
                textPhone.setText(result.get(0).getPhone());
                textCategor.setText(result.get(0).getCategory());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.noprofile)
                        .priority(Priority.HIGH);

                Glide.with(ArticleDetailActivity.this)
                        .load("http://fazilmuammar007.com/blogapp/images/" + result.get(0).getImage())
                        .apply(options)
                        .into(imgImage);

                Glide.with(ArticleDetailActivity.this)
                        .load("http://fazilmuammar007.com/blogapp/images_signature/" + result.get(0).getSignature())
                        .apply(options)
                        .into(ttd);

                loading.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseArticleDetail> call, Throwable t) {
                loading.dismiss();
            }
        });

    }


    //METHOD BUAT BACK DAN SHARE
    @OnClick({R.id.backs, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backs:
                finish();
                break;
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share your awasome story!");
                i.putExtra(Intent.EXTRA_TEXT, "https://www.linkedin.com/company/pt.-integrasi-media-kreasi");
                startActivity(Intent.createChooser(i, "Share URL"));
                break;
        }
    }
}

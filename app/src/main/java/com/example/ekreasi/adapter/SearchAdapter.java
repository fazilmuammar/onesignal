package com.example.ekreasi.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.example.ekreasi.R;
import com.example.ekreasi.activity.ArticleDetailActivity;
import com.example.ekreasi.activity.EditArticleActivity;
import com.example.ekreasi.fragment.ListArticle;
import com.example.ekreasi.helper.MyContants;
import com.example.ekreasi.model.ResponseDelete;
import com.example.ekreasi.model.ResultSearch;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity for Search List Article layout resources
 *
 * Class ini untuk membuat  search adapter  article yang telah dibuat di List Article
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {

    /**
     * Membuat  variabel  ResultSearch yang berfungsi menampung data dan mencari data article di List Article
     */
    List<ResultSearch> data;
    FragmentActivity mContext;


    private String img_path;



    public SearchAdapter(List<ResultSearch> result, FragmentActivity c) {
        this.data = result;
        this.mContext = c;


    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflater = LayoutInflater.from(mContext).inflate(R.layout.cardview_article, parent, false);
        return new MyHolder(inflater);
    }


    /**
     * Membuat method menampilkan data jika search article telah ditemmukan
     */
    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.CONTENT_ID =data.get(position).getContentId();

        holder.email.setText(data.get(position).getTitle());
        holder.category.setText(data.get(position).getCategory());
        holder.tanggals.setText(data.get(position).getTanggal());
        holder.phones.setText(data.get(position).getPhone());
        holder.author.setText(data.get(position).getAuthor());
        img_path=("http://fazilmuammar007.com/blogapp/images/" + data.get(position).getImage());
        holder.img_path=img_path;

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.noprofile)
                .priority(Priority.HIGH);

        Glide.with(mContext)
                .load(img_path)
                .apply(options)
                .into(holder.gambar);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * Membuat method untuk pindah ke Article Detail dari search  list article jika ditemukan
         * untuk melakukan intent disini menggunakan PUT EXTRA dengan parameter CONTENT_ID
         */

        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ArticleDetailActivity.class);
                i.putExtra(MyContants.CONTENT_ID, data.get(position).getContentId());
                mContext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {


        private TextView email,category, tanggals, phones,author,content ;

        ImageView gambar,hapus,edit;

        public String CONTENT_ID,img_path;
        public MyHolder(View itemView) {
            super(itemView);

            author       = (TextView) itemView.findViewById(R.id.text_titles);
            email        = (TextView) itemView.findViewById(R.id.text_title);
            category     = (TextView) itemView.findViewById(R.id.text_category);
            tanggals     = (TextView) itemView.findViewById(R.id.text_created);
            phones       = (TextView) itemView.findViewById(R.id.text_views);
            content      = (TextView) itemView.findViewById(R.id.text_content);
            category     = (TextView) itemView.findViewById(R.id.text_category);

            gambar       = (ImageView) itemView.findViewById(R.id.img_image);

            hapus        =(ImageView)  itemView.findViewById(R.id.hapus);
            edit         = (ImageView) itemView.findViewById(R.id.edit);


            /**
             * Membuat method untuk pindah ke Edit Article dari list article ketika search article ditemukan
             * untuk melakukan intent disini menggunakan PUT EXTRA dengan parameter yang ada dibawah
             */

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah=new Intent(mContext,  EditArticleActivity.class);
                    pindah.putExtra("email",     email.getText().toString());
                    pindah.putExtra("author",    author.getText().toString());
                    pindah.putExtra("category",  category.getText().toString());
                    pindah.putExtra("tanggal",   tanggals.getText().toString());
                    pindah.putExtra("phone",     phones.getText().toString());
                    pindah.putExtra("content",   content.getText().toString());
                    pindah.putExtra("category",  category.getText().toString().toLowerCase());
                    pindah.putExtra("gambar",    img_path);
                    pindah.putExtra("content_id", CONTENT_ID);
                    mContext.startActivity(pindah);
                    ((Activity)mContext).finish();
                }
            });

            /**
             * Panggil Method Hapus
             */
            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Hapus();

                }
            });

        }


        /**
         * method yang digunakan untuk membuat hapus  article di List Article  ketika search article ditemukan dan connect ke server menggunakan retrofit
         * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
         * Parameter yang digunakan di adalah "ResponseDelete" yang terdapat di RestApi
         *
         */
        private void Hapus() {
            final String content_id = CONTENT_ID;
            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Hapus ");
            builder.setMessage("Apakah anda yakin untuk hapus content ini ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RestApi api = InitRetrofit.getInstance();
                    Call<ResponseDelete> regisCall = api.deleteArticle(content_id);
                    regisCall.enqueue(new Callback<ResponseDelete>() {
                        @Override
                        public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                            if (response.isSuccessful()){
                                String result = response.body().getResponse();
                                if (result.equals("success")){
                                    Toast.makeText(mContext, "Content Berhasil Di Hapus", Toast.LENGTH_SHORT).show();

                                    ListArticle.La.MyArticle();

                                }else{
                                    Toast.makeText(mContext, "Gagal Menghapus", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDelete> call, Throwable t) {

                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();




        }
    }


}



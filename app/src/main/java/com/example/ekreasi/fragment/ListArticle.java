package com.example.ekreasi.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.ekreasi.R;
import com.example.ekreasi.adapter.CustomRecycleradapter;
import com.example.ekreasi.adapter.SearchAdapter;
import com.example.ekreasi.model.ResponseListArticle;
import com.example.ekreasi.model.ResponseSearch;
import com.example.ekreasi.model.ResultItemListArticle;
import com.example.ekreasi.model.ResultSearch;
import com.example.ekreasi.network.InitRetrofit;
import com.example.ekreasi.network.RestApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Activity for List Article layout resources
 *
 * Activity ini untuk menampilkan list article yang telah dibuat di Add Article
 *
 * @author Ekreasi
 * @version 1.0.0
 * @since 1.0.0
 */

@SuppressLint("ValidFragment")
public class ListArticle extends Fragment implements SearchView.OnQueryTextListener,MenuItem.OnActionExpandListener {

    /**
     * variabel recyclerview yang berfungsi untuk membuat list
     * variavle swipe yang berfungsi untuk membuat swipe refresh
     */
    RecyclerView recyclerview;
    SwipeRefreshLayout mSwipe;
    ImageView noresult;


    /**
     * Membuat  variabel  ResultItemListArticle untuk menampung data di List Article
     */
    public static ListArticle La;
    private List<ResultItemListArticle> datahistory;
    private Context context;

    /**
     * Membuat class dan variabel ResultSearch untuk mencari data di List Article
     */
    private List<ResultSearch> contacts;
    private SearchAdapter adapter;


    public ListArticle(Context context) {
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myFragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_list, null, false);

        setHasOptionsMenu(true);
        recyclerview = myFragmentView.findViewById(R.id.recyclerview);
        noresult = myFragmentView.findViewById(R.id.noresult);

        mSwipe = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swipe);

        La = this;

        /**
         * Membuat Method Swipe Refresh
         */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyArticle();
                recyclerview.setAdapter(null);
            }
        });

         /**
         * Panggil Method Swipe Refresh
         */
          MyArticle();

        return myFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.widget.SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Article");

        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * method yang digunakan untuk membuat fungsi  List Article connect ke server menggunakan retrofit
     * Membuat method List Article dengan menggunakan CustomRecyclerAdapeter sebagai listnya
     * dan ResultItemListArticle sebagai datanya
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseListArticle" yang terdapat di RestApi
     */
    public void MyArticle() {
        mSwipe.setRefreshing(true);
        InitRetrofit.getInstance().getRequestList().enqueue(new Callback<ResponseListArticle>() {
            @Override
            public void onResponse(Call<ResponseListArticle> call, Response<ResponseListArticle> response) {

                datahistory = response.body().getResult();
                CustomRecycleradapter adapter = new CustomRecycleradapter(datahistory, getActivity());
                recyclerview.setAdapter(adapter);
                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipe.setRefreshing(false);
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<ResponseListArticle> call, Throwable t) {
                mSwipe.setRefreshing(false);
            }
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Article");

    }

    /**
     * method yang digunakan untuk membuat fungsi  Search Article connect ke server menggunakan retrofit
     * Membuat method Search Article dengan menggunakan CustomRecyclerAdapeter sebagai listnya
     * Paramater yang digunakan dapat di lihat di folder network -> RestApi.
     * Parameter yang digunakan di adalah "ResponseSearch" yang terdapat di RestApi
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerview.setVisibility(View.GONE);
        RestApi api = InitRetrofit.getInstance();
        Call<ResponseSearch> call = api.searchArticle(newText);
        call.enqueue(new Callback<ResponseSearch>() {
            @Override
            public void onResponse(Call<ResponseSearch> call, Response<ResponseSearch> response) {
                recyclerview.setVisibility(View.VISIBLE);
                contacts =response.body().getResult();
                adapter = new SearchAdapter(contacts, getActivity());
                recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (contacts.isEmpty()) {
                   noresult.setVisibility(View.VISIBLE);
                } else {
                    noresult.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseSearch> call, Throwable t) {

            }
        });

        return false;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }
}

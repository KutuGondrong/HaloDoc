package com.kutugondrong.halodoc.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.kutugondrong.halodoc.R;
import com.kutugondrong.halodoc.adapter.MainAdapter;
import com.kutugondrong.halodoc.helper.ApiHelper;
import com.kutugondrong.halodoc.helper.ApiURL;
import com.kutugondrong.halodoc.helper.Contanta;
import com.kutugondrong.halodoc.helper.RequestListener;
import com.kutugondrong.halodoc.listener.CustomListViewListener;
import com.kutugondrong.halodoc.model.BaseModel;
import com.kutugondrong.halodoc.model.Data;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerview;
    private SwipeRefreshLayout swipe;
    private MainAdapter mainAdapter;
    private static final String PAGE_KEY = "page";
    private static final String QUERY_KEY = "query";
    private int PAGE_VALUE = 1;

    private static final int INITIAL_REQUEST=11;
    private static final String[] INITIAL_PERMS={Manifest.permission.INTERNET,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(this,INITIAL_PERMS)){
            ActivityCompat.requestPermissions(this, INITIAL_PERMS, INITIAL_REQUEST);
        }
        initToolbar();
        initLayout();
        initAdapter();
        initAction();
        getData("");
    }

    private void initToolbar(){
        Toolbar custom_toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        custom_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        custom_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity.this.finish();
            }
        });
    }

    private void initLayout(){
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipe.setRefreshing(true);
    }

    private void initAdapter(){
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainAdapter = new MainAdapter(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mainAdapter);
    }

    private void initAction(){
        mainAdapter.setCustomListViewListener(new CustomListViewListener<Data>() {
            @Override
            public void onViewClick(Data value) {
                goToDetail(value);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData("");
            }
        });
    }

    private void goToDetail(Data data){
        Intent i = new Intent(this,DetailArticleActivity.class);
        i.putExtra(Contanta.VALUE,data);
        startActivity(i);
    }

    private void getData(String search){
        ApiHelper<Data> apiHelper = new ApiHelper<Data>(ApiURL.BASEURL,ApiHelper.GET);
        apiHelper.addParams(PAGE_KEY,PAGE_VALUE+"");
        if(!search.equals("")){
            apiHelper.addParams(QUERY_KEY,search);
        }
        apiHelper.execute(new Data(), new RequestListener<Data>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Data value) {
                swipe.setRefreshing(false);
                mainAdapter.refresh(value.getDatas());
            }


            @Override
            public void onFailed() {
                swipe.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchArticle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    this.onQueryTextSubmit("");
                }
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchArticle(String search){
        if(search.isEmpty()){
            getData("");
            return;
        }
        getData(search);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INITIAL_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for(int i=0;i<INITIAL_PERMS.length;i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            Log.d("REQUESTEXT","true");
                        }else{
                            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            Log.d("REQUESTEXT","false");
                            return;
                        }
                    }
                }
                return;
            }
        }
    }
}

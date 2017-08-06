package com.kutugondrong.halodoc.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kutugondrong.halodoc.R;
import com.kutugondrong.halodoc.helper.Contanta;
import com.kutugondrong.halodoc.model.Data;


public class DetailArticleActivity extends AppCompatActivity {

    private WebView m_web;
    private String m_szPage	= "http://kutugondrong.com";
    private ProgressBar progressBar;
    private Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        getBunddle();
        initToolbar();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        m_web = (WebView) findViewById(R.id.webview);
        m_web.getSettings().setJavaScriptEnabled(true);
        m_web.getSettings().setBuiltInZoomControls(true);
        m_web.getSettings().setUseWideViewPort(true);
        m_web.getSettings().setLoadWithOverviewMode(true);
//        m_web.setInitialScale(1);
        m_web.setWebViewClient(new MyWebViewClient());
        m_web.loadUrl(m_szPage);
    }

    private void getBunddle(){
        Bundle extras = getIntent().getExtras();
        data = (Data) extras.getSerializable(Contanta.VALUE);
        m_szPage = data.getUrl();
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
                DetailArticleActivity.this.finish();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            DetailArticleActivity.this.setTitle(view.getTitle());
            m_web.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        m_web.onPause();
    }
}

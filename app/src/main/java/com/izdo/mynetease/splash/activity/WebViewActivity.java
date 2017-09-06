package com.izdo.mynetease.splash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.izdo.mynetease.MainActivity;
import com.izdo.mynetease.R;
import com.izdo.mynetease.splash.bean.Action;
import com.orhanobut.logger.Logger;

/**
 * Created by iZdo on 2017/9/5.
 */

public class WebViewActivity extends Activity {

    public static final String ACTION_NAME = "action";

    WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Action action = (Action) getIntent().getSerializableExtra(ACTION_NAME);
        Logger.i(action + "");
        setContentView(R.layout.activity_webview);

        mWebView = (WebView) findViewById(R.id.webView);

        // 启用JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.loadUrl(action.getLink_url());

        // 处理url重定向
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }else{
            Intent intent = new Intent();
            intent.setClass(WebViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}

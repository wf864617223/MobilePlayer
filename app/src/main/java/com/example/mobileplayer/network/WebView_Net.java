package com.example.mobileplayer.network;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mobileplayer.BaseActivity;
import com.example.mobileplayer.R;

public class WebView_Net extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.netlink);
        setRightButton(View.GONE);
        setTitle("网络资源");
        webView = (WebView) findViewById(R.id.webView);
        Bundle bundle = this.getIntent().getExtras();
        String url = bundle.getString("uri");
        //使屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        webView.loadUrl(url);
        //使用JS
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public View setContentView() {
        return View.inflate(WebView_Net.this, R.layout.netlink, null);
    }

    @Override
    public void rightButtonClick() {

    }

    @Override
    public void leftButtonClick() {
        finish();
    }

}

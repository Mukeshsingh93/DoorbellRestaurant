package com.rs.doorbellvendor;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private String username = "", password = "";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        pref = getSharedPreferences("pref",MODE_PRIVATE);
        editor = pref.edit();

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        final WebView webview = new CustomWebView(this,editor);

        setContentView(webview);

        webview.loadUrl("http://doorbellservice.online/vendor_panel/check.php");

        webview.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Android -> JS
                webview.loadUrl("javascript:init('"+username+"','"+password+"')");
            }
        }, 1000);
    }
}

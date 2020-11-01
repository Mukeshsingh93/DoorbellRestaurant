package com.rs.doorbellvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView mWebview;
    ProgressBar loader;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String urls = "";

    private String username = "", password = "", token = "", deviceID = "";

    boolean notification = false;
    String url = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("pref",MODE_PRIVATE);
        editor = pref.edit();


        deviceID = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        notification = getIntent().getBooleanExtra("notification",false);
        if(notification){
            url = getIntent().getStringExtra("url");
        }else{
            username = getIntent().getStringExtra("username");
            password = getIntent().getStringExtra("password");
            token = getIntent().getStringExtra("token");
        }




        callWebView();
    }


    public void callWebView(){


        mWebview = findViewById(R.id.web_view);
        if(Build.VERSION.SDK_INT >= 21){
            mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        mWebview.setWebViewClient(new AdWebViewClient());
        mWebview.getSettings().setJavaScriptEnabled(true);

        if(notification){
            mWebview.loadUrl(url);
        }else{
            mWebview.loadUrl("https://doorbellservice.online/vendor_panel/check.php");
//            mWebview.loadUrl("https://ozzyfood.online/restaurant_panel/check.php");
            mWebview.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //Android -> JS
                    mWebview.loadUrl("javascript:init('"+username+"','"+password+"', '"+ deviceID +"' ,'"+token+"')");
                }
            }, 1000);
        }





    }

    @Override
    public void onBackPressed() {
        if(mWebview!= null && mWebview.canGoBack())
            if(urls.contains("index.php")){
                super.onBackPressed();
            }else{
                mWebview.goBack();
            }
            // if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }

    private class AdWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            urls = url;
            if(!url.contains("check.php")){
                loader.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("response://")) {
                //parse uri
                if(url.contains("200")){
                    view.loadUrl("http://doorbellservice.online/vendor_panel/login.php");
//                    view.loadUrl("http://ozzyfood.online/restaurant_panel/login.php");
                }else if(url.contains("201")){
                    editor.putString("username","").commit();
                    editor.putString("password","").commit();
                    loader.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    loader.setVisibility(View.GONE);
                }


                return true;
            }


            return false;
        }

    }

}

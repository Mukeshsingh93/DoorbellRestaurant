package com.rs.doorbellvendor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CustomWebView extends WebView {

    SharedPreferences.Editor editor;

    public CustomWebView(Context context, SharedPreferences.Editor editor) {
        super(context);
        this.editor = editor;

        setup();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setup() {
        setWebViewClient(new AdWebViewClient());

        getSettings().setJavaScriptEnabled(true);

    }

    private class AdWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("response://")) {
                //parse uri
                if(url.contains("200")){
                    view.loadUrl("http://doorbellservice.online/vendor_panel/login.php");
                }else if(url.contains("201")){
                    editor.putString("username","").commit();
                    editor.putString("password","").commit();
                    Toast.makeText(CustomWebView.this.getContext(), "Invalid username and password", Toast.LENGTH_SHORT).show();


                }


                return true;
            }


            return false;
        }

    }
}

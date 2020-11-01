package com.rs.doorbellvendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);



        pref = getSharedPreferences("pref",MODE_PRIVATE);
        editor = pref.edit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SplashActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
                token = newToken;

                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!pref.getString("username","").equalsIgnoreCase("") && !pref.getString("password","").equalsIgnoreCase("")){
                            startActivity(new Intent(SplashActivity.this,MainActivity.class).putExtra("username",pref.getString("username",""))
                                    .putExtra("password",pref.getString("password","")).putExtra("token",token));
                            finish();
                        }else{
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class).putExtra("token",token));
                            finish();
                        }

                    }
                },3000);

            }
        });


    }
}

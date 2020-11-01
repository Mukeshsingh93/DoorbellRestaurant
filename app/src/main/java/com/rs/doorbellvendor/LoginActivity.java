package com.rs.doorbellvendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView txtLogin;
    private EditText editUser,editPassword;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login);

        pref = getSharedPreferences("pref",MODE_PRIVATE);
        editor = pref.edit();

        token = getIntent().getStringExtra("token");

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);

        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUser.getText().toString().trim().length() != 0 && editPassword.getText().toString().trim().length() != 0){
                    editor.putString("username",editUser.getText().toString()).commit();
                    editor.putString("password",editPassword.getText().toString()).commit();

                    startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("username",editUser.getText().toString())
                    .putExtra("password",editPassword.getText().toString()).putExtra("token",token));


                }else{
                    Toast.makeText(LoginActivity.this, "Please enter username and password first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

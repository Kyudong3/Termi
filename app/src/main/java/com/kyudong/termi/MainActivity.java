package com.kyudong.termi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kyudong.termi.Home.HomeActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private String access_token = "a";
    private String id, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = UserToken.getPreferences(getApplicationContext(), "token");

        //Log.e("token", token);

        if(token.equals("empty")) {
            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
            startActivity(loginIntent);
            finish();
        } else {
            //Log.e("aaa", token);
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            homeIntent.putExtra("authorization", token);
            startActivity(homeIntent);
            finish();
        }

//        SharedPreferences sPref = getSharedPreferences("a", MODE_PRIVATE);
//
//        String token = sPref.getString("authorization", "");
//
//        if(token.equals("")) {
//            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
//            startActivityForResult(loginIntent, REQUEST_CODE);
//            //finish();
//        } else {
//            Log.e("aaa", token);
//            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
//            homeIntent.putExtra("authorization", token);
//            startActivity(homeIntent);
//
//            finish();
//        }
    }

}

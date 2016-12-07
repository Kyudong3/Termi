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

//    private SharedPreferences sPref;
    private SharedPreferences.Editor sEditor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    access_token = data.getStringExtra("authorization");
                    id = data.getStringExtra("prefId");
                    pwd = data.getStringExtra("prefPwd");

                    SharedPreferences sPref = getSharedPreferences("a", MODE_PRIVATE);
                    SharedPreferences.Editor sEditor = sPref.edit();

                    sEditor.putString("authorization", access_token);
                    sEditor.commit();
                    //sEditor = sPref.edit();
//                    sEditor = sPref.edit();
//                    sEditor.remove("authorization");
//                    sEditor.apply();
//
//                    sEditor = sPref.edit();
//                    sEditor.putString("authorization", access_token);
//                    sEditor.putString("ID", id);
//                    sEditor.putString("PWD", pwd);
//                    sEditor.apply();

                    //Toast.makeText(getApplicationContext(), access_token+"", Toast.LENGTH_SHORT).show();
                    Log.e("aaa", access_token);
                    Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    HomeIntent.putExtra("authorization", access_token);
                    startActivity(HomeIntent);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sPref = getSharedPreferences("a", MODE_PRIVATE);

        String token = sPref.getString("authorization", "");

        if(token.equals("")) {
            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
            startActivityForResult(loginIntent, REQUEST_CODE);
        } else {
//            String z = "";
//            CheckAutoLoginPost checkloginPost = new CheckAutoLoginPost();
//            try {
//                checkloginPost.doPostRequest("http://52.78.240.168/api/auth" , z);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Log.e("aaa", token);
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            homeIntent.putExtra("authorization", token);
            startActivity(homeIntent);

            finish();
        }

//        if(access_token.equals("a")) {
//            Intent LoginIntent = new Intent(getApplication(), Login.class);
//            //startActivity(LoginIntent);
//            startActivityForResult(LoginIntent, REQUEST_CODE);
//
//        } else {
//            Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(HomeIntent);
//
//        }

        //finish();
    }

//    public class CheckAutoLoginPost {
//
//        String doPostRequest(String url, String json) throws IOException {
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .header("Content-Type", "application/json")
//                    .addHeader("authorization", )
//                    .url(url)
//                    .post(body)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                }
//            });
//            return null;
//        }
//    }

}

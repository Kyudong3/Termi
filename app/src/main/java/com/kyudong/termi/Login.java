package com.kyudong.termi;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kyudong.termi.Home.HomeActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private CheckBox loginCheckBox;
    private EditText idEditText;
    private EditText pwdEditText;
    private Button loginBtn;
    private TextView signInTxv;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private String postJson;
    private ImageView logoImageView;
    private String token;

    private SharedPreferences mPref;
    private Boolean isFirst;
    private ExplainCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCheckBox = (CheckBox) findViewById(R.id.autoLoginCB);
        idEditText = (EditText) findViewById(R.id.IdEditText);
        pwdEditText = (EditText) findViewById(R.id.pwdEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signInTxv = (TextView) findViewById(R.id.signInTxv);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        mPref = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirst = mPref.getBoolean("isFirst", false);
        if(!isFirst) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();

            dialog = new ExplainCustomDialog();
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            dialog.show(getSupportFragmentManager(), "fragment_dialog");

        } else {

        }

        idEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pwdEditText.getText().length()!=0 && s.length()>0) {
                    loginBtn.setBackgroundResource(R.drawable.loginbtncolor);
                } else {
                    loginBtn.setBackgroundResource(R.drawable.loginbtngray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pwdEditText.getText().length()==0) {
                    loginBtn.setBackgroundResource(R.drawable.loginbtngray);
                }
            }
        });

        pwdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((s.length()>0) && idEditText.getText().length()!=0) {
                    loginBtn.setBackgroundResource(R.drawable.loginbtncolor);
                } else {
                    loginBtn.setBackgroundResource(R.drawable.loginbtngray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = idEditText.getText().toString();
                String pwd = pwdEditText.getText().toString();

                JSONObject idPwdJObj = new JSONObject();

                try {
                    idPwdJObj.put("id", id);
                    idPwdJObj.put("password", pwd);

                    postJson = idPwdJObj.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Post LoginPost = new Post();
                try {
                    LoginPost.doPostRequest("http://52.78.240.168/api/signin", postJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public class Post {

        String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String res = response.body().string();

                    Headers headers = response.headers();

                    token = response.headers().get("authorization");
                    if(loginCheckBox.isChecked()) {
                        UserToken.setPreferences(getApplicationContext(), "token", token);
                    } else {
                        UserToken.setPreferences(getApplicationContext(), "token", "empty");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonOutput = new JSONObject(res);
                                String responseCode = jsonOutput.getString("responseCode");

                                if(responseCode.equals("5")) {
                                    String fcm_token = FirebaseInstanceId.getInstance().getToken();
                                    MyFirebaseInstanceIDService fcm_refresh = new MyFirebaseInstanceIDService();
                                    fcm_refresh.sendRegistrationToServer(fcm_token, token);

                                    Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                    HomeIntent.putExtra("authorization", token);
                                    startActivity(HomeIntent);
                                    finish();
                                } else if(responseCode.equals("6")) {
                                    Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 확인하세요!",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
            return null;
        }
    }
}

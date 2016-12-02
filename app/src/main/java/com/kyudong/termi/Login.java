package com.kyudong.termi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private CheckBox loginCheckBox;
    private EditText idEditText;
    private EditText pwdEditText;
    private Button loginBtn;
    private TextView signInTxv;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private String postJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCheckBox = (CheckBox) findViewById(R.id.autoLoginCB);
        idEditText = (EditText) findViewById(R.id.IdEditText);
        pwdEditText = (EditText) findViewById(R.id.pwdEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signInTxv = (TextView) findViewById(R.id.signInTxv);

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

                Log.e("aa", id);
                Log.e("aaa", pwd);

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
                    final String token = response.headers().get("authorization");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), token + "", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("authorization", token);
                            setResult(RESULT_OK, intent);

                            finish();
                        }
                    });

                }
            });
            return null;
        }
    }
}

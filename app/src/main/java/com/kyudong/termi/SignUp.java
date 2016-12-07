package com.kyudong.termi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    private EditText signInId;
    private EditText signInPwd;
    private Button signInBtn;

    private Toolbar signUpToolbar;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private MyFirebaseInstanceIDService firebaseToken;

    private String signUpPostJson;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInId = (EditText) findViewById(R.id.signInId);
        signInPwd = (EditText) findViewById(R.id.signInPwd);
        signInBtn = (Button) findViewById(R.id.signInBtn);

        signUpToolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(signUpToolbar);

//        String id = signInId.getText().toString();
//        String pwd = signInPwd.getText().toString();

        token = FirebaseInstanceId.getInstance().getToken();

        Log.e("firebase", "token is : " + token);

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String myPhoneNumber = tMgr.getLine1Number();

        Log.e("num", myPhoneNumber + " ");
        JSONObject idPwdTelObject = new JSONObject();

//        try {
//            idPwdTelObject.put("id", id);
//            idPwdTelObject.put("password", pwd);
//            idPwdTelObject.put("telephone", myPhoneNumber);
//
//            signUpPostJson = idPwdTelObject.toString();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String myPhoneNumber = tMgr.getLine1Number();

                Log.e("num", myPhoneNumber + " ");
                JSONObject idPwdTelObject = new JSONObject();

                PostSignIn signUp = new PostSignIn();
                try {
                    idPwdTelObject.put("id", signInId.getText().toString());
                    idPwdTelObject.put("password", signInPwd.getText().toString());
                    idPwdTelObject.put("telephone", myPhoneNumber);
                    idPwdTelObject.put("fcmToken", token);

                    signUpPostJson = idPwdTelObject.toString();

                    signUp.doPostRequest("http://52.78.240.168/api/signup", signUpPostJson);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class PostSignIn {

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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject parentJSONObject = new JSONObject(res);

                                String code = parentJSONObject.getString("responseCode");

                                Toast.makeText(getApplicationContext(), code+"", Toast.LENGTH_SHORT).show();

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

package com.kyudong.termi;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private EditText signInId;
    private EditText signInPwd;
    private Button signInBtn;

    private Toolbar signUpToolbar;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private MyFirebaseInstanceIDService firebaseToken;

    private String signUpPostJson;

    private TextView detailTxv;
    private CheckBox checkBoxx;

    private String token;
    private String fomattedPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkBoxx = (CheckBox) findViewById(R.id.imageButton2);

        detailTxv = (TextView) findViewById(R.id.detailTxv);

        SpannableString content = new SpannableString("상세정보");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        detailTxv.setText(content);

        detailTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClauseActivity.class);
                startActivity(intent);
            }
        });

        signInId = (EditText) findViewById(R.id.signInId);
        signInPwd = (EditText) findViewById(R.id.signInPwd);
        signInBtn = (Button) findViewById(R.id.signInBtn);

        signUpToolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        signUpToolbar.setTitle("");
        setSupportActionBar(signUpToolbar);

        token = FirebaseInstanceId.getInstance().getToken();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBoxx.isChecked() || signInId.getText().length()==0 || signInPwd.getText().length()==0) {
                    Toast.makeText(getApplicationContext(), "모두 작성했는지 확인하세요!", Toast.LENGTH_SHORT).show();
                } else {
                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String myPhoneNumber = tMgr.getLine1Number();

                    if(myPhoneNumber.contains("+")) {
                        fomattedPhoneNumber = myPhoneNumber.replace("+82","0");
                    } else {
                        fomattedPhoneNumber = myPhoneNumber;
                    }

                    JSONObject idPwdTelObject = new JSONObject();

                    PostSignIn signUp = new PostSignIn();
                    try {
                        idPwdTelObject.put("id", signInId.getText().toString());
                        idPwdTelObject.put("password", signInPwd.getText().toString());
                        idPwdTelObject.put("telephone", fomattedPhoneNumber);
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
                                if(code.equals("2")) {
                                    Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_LONG).show();
                                } else if(code.equals("3")) {
                                    Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_LONG).show();
                                } else if(code.equals("4")) {
                                    Toast.makeText(getApplicationContext(),"아이디가 이미 존재합니다",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                                }
                                //Toast.makeText(getApplicationContext(), code+"", Toast.LENGTH_SHORT).show();

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

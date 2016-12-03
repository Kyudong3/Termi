package com.kyudong.termi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Send_Mail extends AppCompatActivity {

    private Toolbar sendToolbar;
    private ImageView backImageView;
    private TextView sendBtnTxv;
    private ImageView addPhoneNum;

    private EditText phoneEditText;
    private EditText writingEditText;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private RadioButton recieveYes;
    private RadioButton recieveNo;
    private RadioGroup radioGroup;

    private CustomDialog dialog;

    private ImageView angel;
    private ImageView devil;
    private LinearLayout llll;

    private String messageType = "a";
    private String token;
    private String postJson;

    private String canReply = "b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__mail);

        Intent intent = getIntent();
        if(intent != null) {
            token = intent.getStringExtra("authorization");
        }

        sendToolbar = (Toolbar) findViewById(R.id.sendToolbar);
//        sendToolbar.setTitle("Send");
        setSupportActionBar(sendToolbar);

        backImageView = (ImageView) findViewById(R.id.backImageView);
        addPhoneNum = (ImageView) findViewById(R.id.addPhoneNum);
        sendBtnTxv = (TextView) findViewById(R.id.sendBtnTxv);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        writingEditText = (EditText) findViewById(R.id.writingEditText);

        radioGroup =(RadioGroup) findViewById(R.id.radiogroup);
        recieveYes = (RadioButton) findViewById(R.id.radioButton);
        recieveNo = (RadioButton) findViewById(R.id.radioButton2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        canReply = "Y";
                        break;
                    case R.id.radioButton2:
                        canReply = "N";
                        break;
                }
            }
        });

//        ActionBar aB = getSupportActionBar();
//        aB.setDisplayShowCustomEnabled(true);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendBtnTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new CustomDialog(Send_Mail.this);
                dialog.show();

            }
        });

        addPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);

//                phoneEditText.setText(mPhoneNumber);
            }
        });
    }

    public class PostSendMail {

        String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .addHeader("authorization", token)
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

                        }
                    });

                }
            });

            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();

            String number = cursor.getString(1);

            phoneEditText.setText(number);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class CustomDialog extends Dialog {

        int commentsId;

        public CustomDialog(Context context) {
            super(context);

            init();
        }

        public CustomDialog(Context context, int themeResId) {
            super(context, themeResId);

            //init(themeResId);
        }

        private void init() {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            angel = (ImageView) findViewById(R.id.angel);
            devil = (ImageView) findViewById(R.id.devil);

            llll = (LinearLayout) findViewById(R.id.llll);

            angel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageType = "G";

                    JSONObject Jobj = new JSONObject();

                    try {
                        Jobj.put("content", writingEditText.getText().toString());
                        Jobj.put("receiverPhoneNumber", phoneEditText.getText().toString());
                        Jobj.put("messageType", messageType);
                        Jobj.put("canReply", canReply);
                        postJson = Jobj.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    PostSendMail sendMail = new PostSendMail();
                    try {
                        sendMail.doPostRequest("http://52.78.240.168/api/message", postJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("content", writingEditText.getText().toString());
                    intent.putExtra("messageType", messageType);
                    setResult(RESULT_OK, intent);
                    finish();
                    //finish();
                }
            });

            devil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageType = "B";

                    JSONObject Jobj = new JSONObject();

                    try {
                        Jobj.put("content", writingEditText.getText().toString());
                        Jobj.put("receiverPhoneNumber", phoneEditText.getText().toString());
                        Jobj.put("messageType", messageType);
                        Jobj.put("canReply", canReply);
                        postJson = Jobj.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    PostSendMail sendMail = new PostSendMail();
                    try {
                        sendMail.doPostRequest("http://52.78.240.168/api/message", postJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("content", writingEditText.getText().toString());
                    intent.putExtra("messageType", messageType);
                    setResult(RESULT_OK, intent);
                    finish();
                    //finish();
                }
            });

        }
    }
}

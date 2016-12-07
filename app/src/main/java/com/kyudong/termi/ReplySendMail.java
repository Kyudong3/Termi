package com.kyudong.termi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ReplySendMail extends AppCompatActivity {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private CustomDialog dialog;
    private String content;
    private int nSeqNo, pos;
    private String token;

    private ImageView imageView, auri, agma;
    private TextView replysendBtn;

    private EditText writingEditText2;
    private String postJson;
    private String messageType ="a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_send_mail);

        Intent intent = getIntent();
        if(intent!=null) {
            nSeqNo = intent.getExtras().getInt("nSeqNo");
            content = intent.getStringExtra("content");
            token = intent.getStringExtra("token");
            pos = intent.getExtras().getInt("pos");
        }

        writingEditText2 = (EditText) findViewById(R.id.writingEditText2);

        Log.e("responseCode : " , token + "  " + nSeqNo + "  " + content);
        imageView = (ImageView) findViewById(R.id.xImageView);
        replysendBtn = (TextView) findViewById(R.id.replysendBtnTxv);
        final RelativeLayout zrelativeLayout = (RelativeLayout) findViewById(R.id.zrelativeLayout);

        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zrelativeLayout.setVisibility(View.GONE);
                //linear.setPadding(0,0,0,50);
            }
        });

        replysendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CustomDialog(ReplySendMail.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog.show();

//                PostReplyMessage postReplyMessage = new PostReplyMessage();
//                try {
//                    postReplyMessage.doPostRequest("http://52.78.240.168/api/reply" + nSeqNo, "");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }

    public class PostReplyMessage {

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
                            JSONObject parentJObject = null;
                            try {
                                parentJObject = new JSONObject(res);
                                String parentJArray = parentJObject.getString("responseCode");
                                Log.e("responseCode", parentJArray);
                                //Toast.makeText(getApplicationContext(), parentJArray , Toast.LENGTH_SHORT).show();

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

    private class CustomDialog extends Dialog {

        int commentsId;

        public CustomDialog(Context context) {
            super(context);

            init();
        }

        public CustomDialog(Context context, int themeResId) {
            super(context, themeResId);

            init();
        }

        private void init() {
            //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            auri = (ImageView) findViewById(R.id.sendauri);
            agma = (ImageView) findViewById(R.id.sendagma);

            auri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageType = "G";

                    JSONObject Jobj = new JSONObject();
                    try {
                        Jobj.put("content", writingEditText2.getText().toString());
                        Jobj.put("messageType", messageType);
                        postJson = Jobj.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    PostReplyMessage postReplyMessage = new PostReplyMessage();
                    try {
                        postReplyMessage.doPostRequest("http://52.78.240.168/api/reply/" + nSeqNo, postJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    Toast.makeText(getApplicationContext(), "어리 답장이 보내졌습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("pos", pos);
                    setResult(RESULT_OK, intent);
                    finish();
                    //finish();
                }
            });

            agma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageType = "B";
                    JSONObject Jobj = new JSONObject();
                    try {
                        Jobj.put("content",writingEditText2.getText().toString());
                        Jobj.put("messageType", messageType);
                        postJson = Jobj.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    PostReplyMessage postReplyMessage = new PostReplyMessage();
                    try {
                        postReplyMessage.doPostRequest("http://52.78.240.168/api/reply/" + nSeqNo, postJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "아그마 답장이 보내졌습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("pos", pos);
                    setResult(RESULT_OK, intent);

                    finish();
                    //finish();
                }
            });

        }
    }
}

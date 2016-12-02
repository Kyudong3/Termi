package com.kyudong.termi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kyudong.termi.InBox.InBoxRvItem;
import com.kyudong.termi.InBox.Inbox;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ReadMessage extends AppCompatActivity {

    int seqNo;
    String content;
    String time;
    String isRead;
    String role;
    String msgType;
    int pos;
    ArrayList<InBoxRvItem> list;
    int a;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private Toolbar readMessageToolbar;
    private TextView deleteBtn;
    private ImageView backBtnImageView;
    private ImageView readImageIcon;
    private TextView readContentTxv;
    private TextView timeTxv;
    private Button btn;
    private String auth;

    long month;
    long day;
    long hour;
    long minute;
    long subtract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        Intent intent =  getIntent();
        if(intent != null) {
            seqNo = intent.getExtras().getInt("seqNo");
            content = intent.getStringExtra("content");
            time = intent.getStringExtra("time");
            isRead = intent.getStringExtra("isRead");
            role = intent.getStringExtra("role");
            msgType = intent.getStringExtra("msgType");
            auth = intent.getStringExtra("authorization");
            pos = intent.getExtras().getInt("pos");
            a = intent.getExtras().getInt("int");
            //list = (ArrayList<InBoxRvItem>) intent.getSerializableExtra("list");

            readMessageToolbar = (Toolbar) findViewById(R.id.readMessageToolbar);
            setSupportActionBar(readMessageToolbar);

            deleteBtn = (TextView) findViewById(R.id.deleteBtnTxv);
            backBtnImageView = (ImageView) findViewById(R.id.backImageView);
            readImageIcon = (ImageView) findViewById(R.id.readImageIcon);
            readContentTxv = (TextView) findViewById(R.id.readContentTxv);
            timeTxv = (TextView) findViewById(R.id.timeTxv);
            btn = (Button) findViewById(R.id.button);

            if(msgType.equals("G")) {
                readImageIcon.setImageResource(R.drawable.read_ouri);
            } else if(msgType.equals("B")) {
                readImageIcon.setImageResource(R.drawable.read_agma);
            }


//            backBtnImageView.setOnClickListener(this);
//            deleteBtn.setOnClickListener(this);
            backBtnImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (role.equals("R")) {
                        //String z = "";
                        DeleteMessage deleteMessage = new DeleteMessage();
                        try {
                            deleteMessage.doDeleteRequest("http://52.78.240.168/api/message/receive/" + seqNo, auth);
                            //Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            //finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (role.equals("S")) {
                        DeleteMessage2 deleteMessage2 = new DeleteMessage2();
                        try {
                            deleteMessage2.doDeleteRequest("http://52.78.240.168/api/message/send/" + seqNo, auth);
                            //Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            //finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String z = "";
                    }

                }
            });

            readContentTxv.setText(content);

            SimpleDateFormat qqqq = new SimpleDateFormat("yyyy.MM.dd HH:mm");

            SimpleDateFormat zxcv = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            zxcv.setTimeZone(TimeZone.getTimeZone("UTC"));
            final Date zz = new Date();
            try {
                Date xxx = zxcv.parse(time);
                String vvvv = qqqq.format(xxx);

                timeTxv.setText(vvvv);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public class DeleteMessage {

        String doDeleteRequest(String url, String auth) throws IOException {
            RequestBody body = RequestBody.create(JSON, auth);
            Request request = new Request.Builder()
                    .addHeader("authorization", auth)
                    .url(url)
                    .delete()
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
                            Intent intent = new Intent();
                            intent.putExtra("pos", pos);
                            intent.putExtra("body", res);
                            intent.putExtra("int", 0);
                            //intent.putExtra("list", list);
                            setResult(RESULT_OK, intent);

                            Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            });
            return null;
        }
    }

    public class DeleteMessage2 {

        String doDeleteRequest(String url, String auth) throws IOException {
            RequestBody body = RequestBody.create(JSON, auth);
            Request request = new Request.Builder()
                    .addHeader("authorization", auth)
                    .url(url)
                    .delete()
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
                            Intent intent = new Intent();
                            intent.putExtra("pos", pos);
                            intent.putExtra("body", res);
                            intent.putExtra("int",1);
                            //intent.putExtra("list", list);
                            setResult(RESULT_OK, intent);

                            Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            });
            return null;
        }
    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.backImageView:
//                finish();
//                break;
//            case R.id.deleteBtnTxv:
//                break;
//
//        }
//    }
}

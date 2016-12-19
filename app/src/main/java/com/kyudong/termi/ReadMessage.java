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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String read_where;
    private static final int REQUEST_CODE2 = 1004;
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
    private String canReply;
    private String isXmas;

    long month;
    long day;
    long hour;
    long minute;
    long subtract;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE2) {
            if(resultCode==RESULT_OK) {
                if(data!=null) {
                    int pos = data.getExtras().getInt("pos");
                    //Log.e("responseCode : ", "wowowowowo success!!");
                    btn.setVisibility(View.GONE);

                    Intent intent = new Intent();
                    intent.putExtra("pos", pos);
                    intent.putExtra("a", "a");
                    intent.putExtra("int", 1);
                    intent.putExtra("diff", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        Intent intent =  getIntent();
        if(intent != null) {

            btn = (Button) findViewById(R.id.button);
            deleteBtn = (TextView) findViewById(R.id.deleteBtnTxv);
            backBtnImageView = (ImageView) findViewById(R.id.backImageView);
            readImageIcon = (ImageView) findViewById(R.id.readImageIcon);
            readContentTxv = (TextView) findViewById(R.id.readContentTxv);
            timeTxv = (TextView) findViewById(R.id.timeTxv);
            readMessageToolbar = (Toolbar) findViewById(R.id.readMessageToolbar);
            readMessageToolbar.setTitle("");
            setSupportActionBar(readMessageToolbar);

            read_where = intent.getStringExtra("int");

            if(read_where.equals("inboxRead")) {
                seqNo = intent.getExtras().getInt("seqNo");
                content = intent.getStringExtra("content");
                time = intent.getStringExtra("time");
                isRead = intent.getStringExtra("isRead");
                role = intent.getStringExtra("role");
                msgType = intent.getStringExtra("msgType");
                auth = intent.getStringExtra("authorization");
                pos = intent.getExtras().getInt("pos");
                canReply = intent.getStringExtra("canReply");
                isXmas = intent.getStringExtra("isXmas");

                if(canReply.equals("N")) {
                    btn.setVisibility(View.GONE);
                }

                backBtnImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("a", "a");
                        intent.putExtra("int", 0);
                        intent.putExtra("pos", pos);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gointent = new Intent(getApplicationContext(), ReplySendMail.class);
                        gointent.putExtra("nSeqNo", seqNo);
                        gointent.putExtra("content", content);
                        gointent.putExtra("token", auth);
                        gointent.putExtra("pos", pos);
                        gointent.putExtra("enIsXmas", isXmas);
                        startActivityForResult(gointent, REQUEST_CODE2);
                    }
                });

                if(msgType.equals("G")) {
                    readImageIcon.setImageResource(R.drawable.read_ouri);
                } else if(msgType.equals("B")) {
                    readImageIcon.setImageResource(R.drawable.read_agma);
                }

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteMessage deleteMessage = new DeleteMessage();
                        try {
                            deleteMessage.doDeleteRequest("http://52.78.240.168/api/message/receive/" + seqNo, auth);
                            //Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            //finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ReadMessageApi readMessageApi = new ReadMessageApi();
                try {
                    readMessageApi.doGetRequest("http://52.78.240.168/api/readMessage/" + seqNo, role);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if(read_where.equals("outboxRead")) {
                seqNo = intent.getExtras().getInt("seqNo");
                content = intent.getStringExtra("content");
                time = intent.getStringExtra("time");
                isRead = intent.getStringExtra("isRead");
                role = intent.getStringExtra("role");
                msgType = intent.getStringExtra("msgType");
                auth = intent.getStringExtra("authorization");
                pos = intent.getExtras().getInt("pos");

                backBtnImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("a", "a");
                        intent.putExtra("int", 1);
                        intent.putExtra("diff", 0);
                        intent.putExtra("pos", pos);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                btn.setVisibility(View.GONE);

                if(msgType.equals("G")) {
                    readImageIcon.setImageResource(R.drawable.read_ouri);
                } else if(msgType.equals("B")) {
                    readImageIcon.setImageResource(R.drawable.read_agma);
                }

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteMessage2 deleteMessage2 = new DeleteMessage2();
                        try {
                            deleteMessage2.doDeleteRequest("http://52.78.240.168/api/message/send/" + seqNo, auth);
                            //Toast.makeText(getApplicationContext(), "글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            //finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

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

    @Override
    public void onBackPressed() {
        if(role.equals("R")) {
            Intent intent = new Intent();
            intent.putExtra("a", "a");
            intent.putExtra("int", 0);
            intent.putExtra("pos", pos);
            setResult(RESULT_OK, intent);
            finish();
        } else if(role.equals("S")) {
            Intent intent = new Intent();
            intent.putExtra("a", "a");
            intent.putExtra("int", 1);
            intent.putExtra("diff", 0);
            intent.putExtra("pos", pos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public class ReadMessageApi {

        String doGetRequest(String url, String role) throws IOException {
            Request request = new Request.Builder()
                    .addHeader("authorization", auth)
                    .url(url)
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
                            intent.putExtra("a", "b");
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
                            intent.putExtra("a", "b");
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

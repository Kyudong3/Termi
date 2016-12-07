package com.kyudong.termi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class UnRead extends AppCompatActivity {

    RelativeLayout readBGLLayout;
    private int seqNo;
    private String content, msgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_read);

        readBGLLayout = (RelativeLayout) findViewById(R.id.readbackGroundLL);

        Intent intent = getIntent();
        if(intent != null) {
            seqNo = intent.getExtras().getInt("seqNo");
            content = intent.getStringExtra("content");
            msgType = intent.getStringExtra("msgType");

            if(msgType.equals("G")) {
                readBGLLayout.setBackgroundResource(R.drawable.dialogreadmessageauri);
            } else if(msgType.equals("B")) {
                readBGLLayout.setBackgroundResource(R.drawable.dialogreadmessageagma);
            }
        }
    }
}

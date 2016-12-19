package com.kyudong.termi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ClauseActivity extends AppCompatActivity {

    private TextView textView10;
    private ImageView clauseIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);

        textView10 = (TextView) findViewById(R.id.textView10);
        textView10.setMovementMethod(new ScrollingMovementMethod());

        clauseIV = (ImageView) findViewById(R.id.clausebackImageView);
        clauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

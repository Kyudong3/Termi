package com.kyudong.termi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001;

    private String access_token = "a";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    access_token = data.getStringExtra("authorization");

                    Toast.makeText(getApplicationContext(), access_token+"", Toast.LENGTH_SHORT).show();

                    Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    HomeIntent.putExtra("authorization", access_token);
                    startActivity(HomeIntent);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this, SplashActivity.class));

        if(access_token.equals("a")) {
            Intent LoginIntent = new Intent(getApplication(), Login.class);
            //startActivity(LoginIntent);
            startActivityForResult(LoginIntent, REQUEST_CODE);

            //finish();
        } else {
            Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(HomeIntent);
        }
    }
}

package com.kyudong.termi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Send_Mail extends AppCompatActivity {

    private Toolbar sendToolbar;
    private Button btn2;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__mail);

        sendToolbar = (Toolbar) findViewById(R.id.sendToolbar);
//        sendToolbar.setTitle("Send");
        setSupportActionBar(sendToolbar);

        ActionBar aB = getSupportActionBar();
        aB.setDisplayShowCustomEnabled(true);

        btn2 = (Button) findViewById(R.id.button2);
        phoneEditText = (EditText) findViewById(R.id.editText);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

            cursor.moveToFirst();
            String sName = cursor.getString(0);
            String sNumber = cursor.getString(1);
            cursor.close();

            Toast.makeText(getApplicationContext(), sNumber+" " , Toast.LENGTH_SHORT).show();

            phoneEditText.setText(sName);

        }
    }
}

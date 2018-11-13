package com.example.jh56t.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Edit extends AppCompatActivity {
    EditText etName, etIP, etPort, etAccount, etPassword, etRemark;   // EditText
    public static MainActivity mainActivity;    // Implements of MainActivity class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initWork(); // Initial settings
        getIntentData();  // Get the data from the Intent in MainActivity
    }

    // Initial settings
    private void initWork() {
        // EditText
        etName = (EditText) findViewById(R.id.etName);
        etIP = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRemark = (EditText) findViewById(R.id.etRemark);

        // MainActivity class
        mainActivity= new MainActivity();
    }

    // Get the data from the Intent in MainActivity
    private void getIntentData() {
        Intent it_edit = getIntent();
        String[] s = new String[]{it_edit.getStringExtra("name"), it_edit.getStringExtra("ip"),
                                it_edit.getStringExtra("port"), it_edit.getStringExtra("account"),
                                it_edit.getStringExtra("password"), it_edit.getStringExtra("remark"),
                                it_edit.getStringExtra("state")};

        // 判斷按下的為編輯鈕則將ListView中的資料傳至EditText中顯示
        if(s[6].equals("edit")){
            // Show the gotten data
            etName.setText(s[0]);
            etIP.setText(s[1]);
            etPort.setText(s[2]);
            etAccount.setText(s[3]);
            etPassword.setText(s[4]);
            etRemark.setText(s[5]);
        }
    }

    // 儲存按鈕監聽事件
    public void onSave(View view){
        // Get the data from the EditText
        String nameStr = etName.getText().toString().trim();
        String ipStr = etIP.getText().toString().trim();
        String portStr = etPort.getText().toString().trim();
        String accountStr = etAccount.getText().toString().trim();
        String passwordStr = etPassword.getText().toString().trim();
        String remarkStr = etRemark.getText().toString().trim();

        // Return the data
        Intent it_save = new Intent();
        it_save.putExtra("name", nameStr);
        it_save.putExtra("ip", ipStr);
        it_save.putExtra("port", portStr);
        it_save.putExtra("account", accountStr);
        it_save.putExtra("password", passwordStr);
        it_save.putExtra("remark", remarkStr);
        if(getIntentData().s6)
        it_save.putExtra("state", "edit");

        setResult(RESULT_OK, it_save);
        finish();
    }

    // 取消按鈕監聽事件
    public void onCancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}

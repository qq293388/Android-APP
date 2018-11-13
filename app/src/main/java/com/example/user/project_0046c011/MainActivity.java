package com.example.user.project_0046c011;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button btn_save;
    private Button btn_restore;
    private Button btn3;
    private Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //editText = (EditText)findViewById(R.id.editText);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_restore = (Button) findViewById(R.id.btn_restore);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(listener);
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(listener);

        btn_save.setOnClickListener(saveClickListener);
        btn_restore.setOnClickListener(restoreClickListener);
        Spinner spn1 = (Spinner) findViewById(R.id.spn1);
        final String[] Pos_Mode = {"kinematic" , "static"};
        ArrayAdapter<String> Pos_ModeList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                Pos_Mode);

        spn1.setAdapter(Pos_ModeList);

        Spinner spn2 = (Spinner) findViewById(R.id.spn2);
        final String[] wideLane = {"L1", "L2", "ion-free", "L1/L2 wide-lane", "L5", "L1/L5 wide-lane"};
        ArrayAdapter<String> wideLaneList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                wideLane);

        spn2.setAdapter(wideLaneList);
    }

    String[] MeasurementType = {"Measurement Type", "1->RTCM3.2", "2->UBlox", "3->MTK"};
    String[] MTK_FW_TYPE = {"MTK F/W TYPE" , "0: Orignal" , "1: Locosys" , "2:Sierra"};

    private Button.OnClickListener listener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            final View test = v;
            String[] temp = {};

            final String[] items = new String[3];
            if (v.getId() == R.id.btn3) {
                temp = MeasurementType;
            } else if (v.getId() == R.id.btn4) {
                temp = MTK_FW_TYPE;
            }
                items[0] = temp[1];
                items[1] = temp[2];
                items[2] = temp[3];


            AlertDialog.Builder listDialog = new AlertDialog.Builder(MainActivity.this);
            listDialog.setTitle(temp[0]);
            listDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (test.getId() == R.id.btn3)
                        btn3.setText(items[which]);
                    if (test.getId() == R.id.btn4)
                        btn4.setText(items[which]);
                }
            });
            listDialog.show();
        }
    };

//    public void onClick(View v) {
//        if(v.getId()==R.id.btn3){
//            final String[] items = {"RTCM3.2" , "UBlox" , "MTK"};
//            AlertDialog.Builder listDialog = new AlertDialog.Builder(MainActivity.this);
//            listDialog.setTitle("Measurement Type");
//
//            listDialog.setItems(items, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, "You click " + items[which], Toast.LENGTH_SHORT).show();
//                    btn3.setText(items[which]);
//                }
//            } );
//            listDialog.show();
//        }

    private Button.OnClickListener saveClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences SharedPref = getSharedPreferences("Pref", 0);

            //寫入資料
            SharedPreferences.Editor editor = SharedPref.edit();
            editor.putString("Name", editText.getText().toString());
            editor.commit();
        }
    };

    private Button.OnClickListener restoreClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            SharedPreferences SharedPref = getSharedPreferences("Pref", 0);

            String name = SharedPref.getString("name", "");
            editText.setText(name);
        }
    };
}

package com.example.jh56t.project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.jh56t.project.Edit;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    // The settings of database
    static final String DB_NAME="ServerDB"; // Database
    static final String TB_NAME="serverList";   // Table
    static final String[] FORM = new String[]{"name", "ip", "port", "account", "password", "remark"};   // the details in table
    SQLiteDatabase db;

    // The settings of listView
    ListView listView;
    static final int MAX=8; // the maximum number of listView
    SimpleCursorAdapter adapter;
    Cursor cur;
    int cursorID;   // the id of the listView's item

    // ImageButton
    ImageButton imbtnInsert, imbtnUpdate, imbtnDelete;

    // Implement of Edit class
    public static Edit edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWork(); // Initial settings
        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);    // Open or create database
        createTable();  // Create table in the database
        cur = db.rawQuery("SELECT * FROM " + TB_NAME, null);
        // Insert the data initially if the database doesn't have any data
        if(cur.getCount()==0){
            addData("NTOU CNCE", "140.121.131.81", "6000", "NTOUCNCE", "ntoucnceAdmin", "NTOU CNCE");
        }


        // ListView's settings
        listView = (ListView)findViewById(R.id.listView);
        adapter = new SimpleCursorAdapter(this, R.layout.item, cur, FORM, new int[]{R.id.name, R.id.ip, R.id.port, R.id.account, R.id.password, R.id.remark}, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        requery();
    }

    // Initial settings
    private void initWork() {
        // ImageButton
        imbtnInsert = (ImageButton) findViewById(R.id.imbtnInsert);
        imbtnUpdate = (ImageButton) findViewById(R.id.imbtnUpdate);
        imbtnUpdate.setEnabled(false);  // close the button initially
        imbtnDelete = (ImageButton) findViewById(R.id.imbtnDelete);
        imbtnDelete.setEnabled(false);  // close the button initially

        // Edit class
        edit = new Edit();
    }

    // Create table in the database
    private void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TB_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(32), " + "ip VARCHAR(32), " + "port VARCHAR(32), " + "account VARCHAR(32), " + "password VARCHAR(32), " + "remark VARCHAR(64))";
        db.execSQL(createTable);
    }

    // The method of requery
    private void requery() {
        cur = db.rawQuery("SELECT * FROM " + TB_NAME, null);
        adapter.changeCursor(cur);

        if(cur.getCount()==MAX)
            imbtnInsert.setEnabled(false);
        else
            imbtnInsert.setEnabled(true);
    }

    // Add data to the table
    private void addData(String name, String ip, String port, String account, String password, String remark) {
        ContentValues cv = new ContentValues(6);

        cv.put(FORM[0], name);
        cv.put(FORM[1], ip);
        cv.put(FORM[2], port);
        cv.put(FORM[3], account);
        cv.put(FORM[4], password);
        cv.put(FORM[5], remark);

        db.insert(TB_NAME, null, cv);
    }

    // Update data in the table
    private void update(String name, String ip, String port, String account, String password, String remark, int id){
        ContentValues cv = new ContentValues(6);

        cv.put(FORM[0], name);
        cv.put(FORM[1], ip);
        cv.put(FORM[2], port);
        cv.put(FORM[3], account);
        cv.put(FORM[4], password);
        cv.put(FORM[5], remark);

        db.update(TB_NAME, cv, "_id=" + id, null);
    }



    // ListView按鈕監聽事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cur.moveToPosition(position);   // Point to the item

        // Open the Update and Delete buttons
        imbtnUpdate.setEnabled(true);
        imbtnDelete.setEnabled(true);
    }

    // 回傳其他Intent的參數值
    protected void onActivityResult(int requestCode, int resultCode, Intent it){
        String nameStr = "", ipStr = "", portStr = "", accountStr = "", passwordStr = "", remarkStr = "", state = "";
        if (resultCode == RESULT_OK) {
            nameStr = it.getStringExtra("name");
            ipStr = it.getStringExtra("ip");
            portStr = it.getStringExtra("port");
            accountStr = it.getStringExtra("account");
            passwordStr = it.getStringExtra("password");
            remarkStr = it.getStringExtra("remark");
            state = it.getStringExtra("state");
            adapter.notifyDataSetChanged();

            if (state.equals("edit")) {
                update(nameStr, ipStr, portStr, accountStr, passwordStr, remarkStr, cursorID);
                requery();
            } else if (state.equals("add")) {
                addData(nameStr, ipStr, portStr, accountStr, passwordStr, remarkStr);
                requery();
            }
        }
    }

    // Insert按鈕監聽事件
    public void onInsert(View view){
        // Close the Update and Delete buttons
        imbtnUpdate.setEnabled(false);
        imbtnDelete.setEnabled(false);

        Intent it_insert = new Intent(this, Edit.class);
        it_insert.putExtra("state", "add");
        startActivityForResult(it_insert, 0);
    }

    // Update按鈕監聽事件
    public void onUpdate(View view){
        // Close the Update and Delete buttons
        imbtnUpdate.setEnabled(false);
        imbtnDelete.setEnabled(false);

        cursorID = cur.getInt(0);
        Intent it_edit = new Intent(this, Edit.class);
        it_edit.putExtra("name", cur.getString(cur.getColumnIndex(FORM[0])));
        it_edit.putExtra("ip", cur.getString(cur.getColumnIndex(FORM[1])));
        it_edit.putExtra("port", cur.getString(cur.getColumnIndex(FORM[2])));
        it_edit.putExtra("account", cur.getString(cur.getColumnIndex(FORM[3])));
        it_edit.putExtra("password", cur.getString(cur.getColumnIndex(FORM[4])));
        it_edit.putExtra("remark", cur.getString(cur.getColumnIndex(FORM[5])));
        it_edit.putExtra("state", "edit");
        startActivityForResult(it_edit, cursorID);
    }

    // Delete按鈕監聽事件
    public void onDelete(View view){
        // Close the Update and Delete buttons
        imbtnUpdate.setEnabled(false);
        imbtnDelete.setEnabled(false);

        db.delete(TB_NAME, "_id=" + cur.getInt(0), null);
        requery();
    }
}

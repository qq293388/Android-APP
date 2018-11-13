package com.example.user.ntripclient2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static final String DB_NAME="NtripsourceDB";
    static final String TB_NAME="MountpointTB";
    static final String[] FROM = new String[]
            {"type","mountpoint","identifier","format","formatdetails","carrier",
                    "navsystem","network","country","latitude","longitude","nmea",
                    "solution","generator","compression","authentication","fee","bitrate"};
    SQLiteDatabase db;
    Cursor cur;
    SimpleCursorAdapter adapter;
    ListView listView;
    private TextView textview;
    private Button bt_show;
    private Button bt_connect;
    private Button bt_update;
    private Socket clientSocket;
    private BufferedReader br;
    private Thread thread;
    static String recivedword,allrecivedword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView)findViewById(R.id.text);
        bt_connect = (Button)findViewById(R.id.bt_connent);
        bt_show = (Button)findViewById(R.id.show);
        bt_update = (Button)findViewById(R.id.update);
        //建立資料庫
        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null);
        //建立資料表
        String creatTable="CREATE TABLE IF NOT EXISTS " + TB_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type VARCHAR(32), " + "mountpoint VARCHAR(32), " + "identifier VARCHAR(32), " +
                "format VARCHAR(32), " + "formatdetails VARCHAR(64), " + "carrier VARCHAR(32), " +
                "navsystem VARCHAR(64), " + "network VARCHAR(32), " + "country VARCHAR(32), " +
                "latitude VARCHAR(32), " + "longitude VARCHAR(32), " + "nmea VARCHAR(32), " +
                "solution VARCHAR(32), " + "generator VARCHAR(32), " + "compression VARCHAR(32), " +
                "authentication VARCHAR(32), " + "fee VARCHAR(32), " + "bitrate VARCHAR(32))";
        db.execSQL(creatTable);
        cur = db.rawQuery("SELECT * FROM "+ TB_NAME,null);
        //建立adapter
        adapter =new SimpleCursorAdapter(this,R.layout.forlistview,  cur,FROM,new int[]
                {R.id.mountpoint},0);
        listView=(ListView)findViewById(R.id.lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        requery();
    }

    private void addData(String[] args){
        ContentValues cv = new ContentValues(18);
        cv.put(FROM[0], args[0]);
        cv.put(FROM[1], args[1]);
        cv.put(FROM[2], args[2]);
        cv.put(FROM[3], args[3]);
        cv.put(FROM[4], args[4]);
        cv.put(FROM[5], args[5]);
        cv.put(FROM[6], args[6]);
        cv.put(FROM[7], args[7]);
        cv.put(FROM[8], args[8]);
        cv.put(FROM[9], args[9]);
        cv.put(FROM[10], args[10]);
        cv.put(FROM[11], args[11]);
        cv.put(FROM[12], args[12]);
        cv.put(FROM[13], args[13]);
        cv.put(FROM[14], args[14]);
        cv.put(FROM[15], args[15]);
        cv.put(FROM[16], args[16]);
        cv.put(FROM[17], args[17]);

        db.insert(TB_NAME,null,cv);
    }

    private  void updateData(String[] args,int id){
        ContentValues cv = new ContentValues(18);
        cv.put(FROM[0], args[0]);
        cv.put(FROM[1], args[1]);
        cv.put(FROM[2], args[2]);
        cv.put(FROM[3], args[3]);
        cv.put(FROM[4], args[4]);
        cv.put(FROM[5], args[5]);
        cv.put(FROM[6], args[6]);
        cv.put(FROM[7], args[7]);
        cv.put(FROM[8], args[8]);
        cv.put(FROM[9], args[9]);
        cv.put(FROM[10], args[10]);
        cv.put(FROM[11], args[11]);
        cv.put(FROM[12], args[12]);
        cv.put(FROM[13], args[13]);
        cv.put(FROM[14], args[14]);
        cv.put(FROM[15], args[15]);
        cv.put(FROM[16], args[16]);
        cv.put(FROM[17], args[17]);

        db.update(TB_NAME,cv, "_id="+id,null);
    }

    private void requery() {
        cur = db.rawQuery("SELECT * FROM "+TB_NAME,null);
        adapter.changeCursor(cur);
        if(cur.getCount() ==0)
            bt_update.setEnabled(true);
        else
            bt_update.setEnabled(false);
    }
    //連結socket伺服器做傳送與接收
    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            try{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText("開始連線");
                    }
                });
                // IP為Server端
                InetAddress serverIp = InetAddress.getByName("106.104.5.66");
                int serverPort = 5000;
                clientSocket = new Socket(serverIp, serverPort);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText("連線成功");
                    }
                });
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                pw.print("GET / HTTP/1.1\r\n\r\n");
                pw.flush();
                // 取得網路訊息
                while (true) {
                    recivedword = br.readLine();    //宣告一個緩衝,從br串流讀取值
                    // 如果不是空訊息
                    if (recivedword == null)
                        break;
                    allrecivedword = allrecivedword+recivedword;
                }
                pw.close();
                br.close();
                clientSocket.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText("結束連線");
                    }
                });
            }
            catch(Exception e){
                //當斷線時會跳到catch,可以在這裡寫上斷開連線後的處理
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText("發生錯誤");
                    }
                });
            }
        }
    };
    public void connect(View view) {
        db.delete(TB_NAME,null,null);
        thread = new Thread(Connection);
        thread.start();
        requery();
        bt_show.setEnabled(false);
    }

    public void show(View view){
            String[] args=new String[18];
        for(int i =0;i<18;i++) { args[i]=cur.getString(i+1); }
        Intent it = new Intent(this,Main2Activity.class);
        it.putExtra("MountpointTable",args);
        startActivity(it);
    }

    public void update(View view) {
       if(allrecivedword!=null){
            String tmp= allrecivedword.substring(allrecivedword.indexOf("STR"),allrecivedword.indexOf("ENDSOURCETABLE"));
            String[] databox = new String[18];
            int count=0;
            do {
                if(count!=0)
                    tmp = tmp.substring(1);
                databox[0] = tmp.substring(0,tmp.indexOf(";"));
                tmp = tmp.substring(tmp.indexOf(";")+1);
                for (int a = 1; a < 17; a++) {
                    databox[a] = tmp.substring(0,tmp.indexOf(";"));
                    tmp = tmp.substring(tmp.indexOf(";")+1);
                }
                databox[17]= tmp.substring(0,tmp.indexOf(";"));
                tmp = tmp.substring(tmp.indexOf(";"));
                addData(databox);
                requery();

                count++;
            }while(tmp.length()!=1);
        }
        else
            return;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        cur.moveToPosition(i);
        bt_show.setEnabled(true);
    }
}

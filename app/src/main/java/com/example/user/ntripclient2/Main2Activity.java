package com.example.user.ntripclient2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    TextView type,mountpoint,identifier,format,format_details,carrier,nav_system,network,country,
            latitude,longitude,nmea,solution,generator,compr_encryp,authentication,fee,bitrate;
    String[] args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        args = new String[18];
        type = (TextView)findViewById(R.id.type);
        mountpoint = (TextView)findViewById(R.id.mountpoint);
        identifier = (TextView)findViewById(R.id.identifier);
        format = (TextView)findViewById(R.id.format);
        format_details = (TextView)findViewById(R.id.format_details);
        carrier = (TextView)findViewById(R.id.carrier);
        nav_system = (TextView)findViewById(R.id.nav_system);
        network = (TextView)findViewById(R.id.network);
        country = (TextView)findViewById(R.id.country);
        latitude = (TextView)findViewById(R.id.latitude);
        longitude = (TextView)findViewById(R.id.longitude);
        nmea = (TextView)findViewById(R.id.nmea);
        solution = (TextView)findViewById(R.id.solution);
        generator = (TextView)findViewById(R.id.generator);
        compr_encryp = (TextView)findViewById(R.id.compr_encryp);
        authentication = (TextView)findViewById(R.id.authentication);
        fee = (TextView)findViewById(R.id.fee);
        bitrate = (TextView)findViewById(R.id.bitrate);
        Intent it = getIntent();
        args = it.getStringArrayExtra("MountpointTable");
        type.setText(args[0]);
        mountpoint.setText(args[1]);
        identifier.setText(args[2]);
        format.setText(args[3]);
        format_details.setText(args[4]);
        carrier.setText(args[5]);
        nav_system.setText(args[6]);
        network.setText(args[7]);
        country.setText(args[8]);
        latitude.setText(args[9]);
        longitude.setText(args[10]);
        nmea.setText(args[11]);
        solution.setText(args[12]);
        generator.setText(args[13]);
        compr_encryp.setText(args[14]);
        authentication.setText(args[15]);
        fee.setText(args[16]);
        bitrate.setText(args[17]);
    }
}

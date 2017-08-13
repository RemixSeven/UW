package com.jiadi.uw;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.jiadi.uw.tools.*;


public class Wifisetting extends AppCompatActivity {

    private Button Bt_save;
    private Button Bt_back;
    private EditText Led_edit;
    private Editable led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bind();
        Bt_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                led = Led_edit.getText();
            }
        });
        Bt_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wifisetting.this, SubmarineActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bind() {
        Bt_save = tools.find(this, R.id.bt_save);
        Bt_back = tools.find(this, R.id.bt_back);
        Led_edit = tools.find(this, R.id.led_edit);
    }
}

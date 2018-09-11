package com.kirilldrob.h8background;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ServiceConnection sConn;
    boolean bound = false;
    final String LOG_TAG = "h8_Logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       findViewById(R.id.btn_Intent).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
        MyIntentService.startServiceWork(MainActivity.this);
           }
       });

        findViewById(R.id.btn_Service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//---------------------------
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
        Intent intent = new Intent(this, MyService.class);
        bindService(intent,sConn,BIND_AUTO_CREATE);
        subscribeForProgressUpdates();


    }
}

package com.kirilldrob.h8background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //ServiceConnection sConn;
   // boolean bound = false;
   // final String LOG_TAG = "h8_Logs";
    TextView tv;
    public final static String BROADCAST_ACTION = "kirilldrob.servicebackbroadcast";
    public final static String PARAM_PROGRESS = "progress";
    BroadcastReceiver br;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv_Progress);

        findViewById(R.id.btn_Intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyIntentService.startServiceWork(MainActivity.this);
            }
        });

        findViewById(R.id.btn_Service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyService.startServiceWork(MainActivity.this);

            }
        });
        subscribeForProgressUpdates();
    }

    public void subscribeForProgressUpdates() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        // создаем BroadcastReceiver
        BroadcastReceiver br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra(PARAM_PROGRESS, 0)+1;
                tv.setText(task + "%");
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        localBroadcastManager.registerReceiver(br, intFilt);
    }

    @Override
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(br);
        //??? localBroadcastManager=null;  смысла нету, ведь ссылка на него хранится в статич. поле синглтона,
        // тем более  Активити возможно будет уничтожено..
        //stopService(intent)  - нету смысла?
        super.onStop();
    }
}



/*------------------Alternative way for future---------

//        Intent intent = new Intent(this, MyService.class);
//        bindService(intent,sConn,BIND_AUTO_CREATE);
//        subscribeForProgressUpdates();



        sConn = new ServiceConnection() {
public void onServiceConnected(ComponentName name, IBinder binder) {
        IBinder.setActivity(this);
        Log.d(LOG_TAG, "MainActivity onServiceConnected");
        bound = true;
        }

public void onServiceDisconnected(ComponentName name) {
        Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
        bound = false;
        }
        };*/
package com.kirilldrob.h8background;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class MyService extends Service {

    MyWorkerThread mWorkerThread;
    boolean isDestroyed;

    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void startServiceWork(Context context) {
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  Message msg = mServiceHandler.obtainMessage(); Alternative way
        // msg.arg1 = startId;

        //---------HandlerThread
        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < 100 && !isDestroyed; i++) {
                        Thread.sleep(100);
                        notifyUI(i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        if (mWorkerThread == null) {
            mWorkerThread = new MyWorkerThread("myWorkerThread");
            mWorkerThread.start();
            mWorkerThread.prepareHandler();
        }
        //--------Alternative WAY:
        // mWorkerThread.send(msg);
        mWorkerThread.postTask(task);

        return START_STICKY;
    }


    public void notifyUI(int progress) {
        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
        intent.putExtra(MainActivity.PARAM_PROGRESS, progress);
        // Send local broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


    @Override
    public void onDestroy() {
        mWorkerThread.quit();  // обязательно убить поток!
        super.onDestroy();
    }

    public class MyWorkerThread extends HandlerThread {

        private Handler mWorkerHandler;

        public MyWorkerThread(String name) {
            super(name);
        }

        public void postTask(Runnable task) {
            mWorkerHandler.post(task);
        }

        public void prepareHandler() {
            mWorkerHandler = new Handler(getLooper());
        }
/*----------------Alternative way of sending msg ------
        //My own realization
        public void send(Message msg){
            mWorkerHandler.sendMessage(msg);
        }
          public class MyHandler extends Handler {
              @Override
              public void handleMessage(Message msg) {
                 // Thread.sleep();....
                  super.handleMessage(msg);
              }
         }        */


    }


}



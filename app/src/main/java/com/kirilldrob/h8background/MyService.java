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

public class MyService extends Service {

    Handler mServiceHandler = new Handler();
    MyWorkerThread mWorkerThread;
    boolean isDestroyed;

    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // @Override
    // public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    //throw new UnsupportedOperationException("Not yet implemented");
    //  }

    public static void startServiceWork(Context context, ServiceConnection sConn) {
        Intent intent = new Intent(context, MyService.class);
        //intent.setAction(ACTION_FOO);
        //  intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        //Перенести в OnCreate of Activity


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;

        //---------HandlerThread
        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < 100 && !isDestroyed; i++) {
                        Thread.sleep(100);
                        // notifyUI();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } };

       if (mWorkerThread==null) {
           mWorkerThread = new MyWorkerThread("myWorkerThread");
           mWorkerThread.start();
           mWorkerThread.prepareHandler();
           mWorkerThread.postTask(task);
       }
        //--------
       // mServiceHandler.sendMessage(msg);
       mWorkerThread.send(msg);
       mWorkerThread.postTask(task);


       return START_STICKY;
    }


    @Override
    public void onDestroy() {
        mWorkerThread.quit();
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
        //My own realization
        public void send(Message msg){
            mWorkerHandler.sendMessage(msg);
          //  mWorkerHandler.handleMessage(msg){
           //                 };
        }


        public void prepareHandler() {
            mWorkerHandler = new Handler(getLooper());
        }
    }

}



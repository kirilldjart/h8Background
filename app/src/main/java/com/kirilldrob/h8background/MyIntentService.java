package com.kirilldrob.h8background;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyIntentService extends IntentService {
    boolean isDestroyed;

    public MyIntentService() {
        super("MyIntentService");
    }



    public static void startServiceWork(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        showToast("Starting Intent Service");

        try {
            for (int i = 0; i < 100 && !isDestroyed; i++) {
                Thread.sleep(100);
                notifyUI(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        showToast("Finishing IntentService");
    }


    public void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MyIntentService.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    //




    public void notifyUI(int progress) {

        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
        intent.putExtra(MainActivity.PARAM_PROGRESS, progress);
        // Send local broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


}

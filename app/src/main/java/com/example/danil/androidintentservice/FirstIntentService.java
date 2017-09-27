package com.example.danil.androidintentservice;

import android.app.IntentService;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FirstIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_MYINTENTSERVICE = "com.example.danil.androidintentservice.RESPONSE";

    public FirstIntentService() {
        super("FirstIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            handleStartAction();
            int time = intent.getIntExtra(FirstActivity.PARAM_TIME, 0);
            for(int i=0;i<=time;i++){
                TimeUnit.SECONDS.sleep(1);
                handleIntermediateAction(i);
            }
            TimeUnit.SECONDS.sleep(1);
            String task = intent.getStringExtra(FirstActivity.PARAM_TASK);
            handleFinishAction(task);
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleStartAction() {
        Intent responseIntent = new Intent();
        responseIntent.setAction(FirstActivity.STATUS_START);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(responseIntent);
    }

    private void handleIntermediateAction(int result) {
        Intent responseIntent = new Intent();
        responseIntent.setAction(FirstActivity.STATUS_INTERMEDIATE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(FirstActivity.PARAM_RESULT, result);
        sendBroadcast(responseIntent);
    }

    private void handleFinishAction(String task) {
        Intent responseIntent = new Intent();
        responseIntent.setAction(FirstActivity.STATUS_FINISH);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(FirstActivity.PARAM_TASK, task);
        sendBroadcast(responseIntent);
    }
}

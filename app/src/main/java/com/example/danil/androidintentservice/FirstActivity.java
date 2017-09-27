package com.example.danil.androidintentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    Intent firstIntentService;

    public final static String STATUS_START = "START";
    public final static String STATUS_INTERMEDIATE = "INTERMEDIATE";
    public final static String STATUS_FINISH = "FINISH";

    public final static String PARAM_TIME = "time";
    public final static String PARAM_TASK = "task";
    public final static String PARAM_RESULT = "result";

    private TextView textViewInfo;
    private TextView textViewResult;
    private TextView textViewTime;
    private Button btnOk;
    private SeekBar seekBar;

    private StartBroadcastReceiver startBroadcastReceiver;
    private IntermediateBroadcastReceiver intermediateBroadcastReceiver;
    private FinishBroadcastReceiver finishBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTime.setText(String.valueOf(seekBar.getProgress()));

        btnOk = (Button) findViewById(R.id.buttonStart);
        btnOk.setOnClickListener(onClickListener);

        startBroadcastReceiver = new StartBroadcastReceiver();
        intermediateBroadcastReceiver = new IntermediateBroadcastReceiver();
        finishBroadcastReceiver = new FinishBroadcastReceiver();

        IntentFilter startIntentFilter = new IntentFilter(STATUS_START);
        startIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(startBroadcastReceiver, startIntentFilter);

        IntentFilter intermediateIntentFilter = new IntentFilter(STATUS_INTERMEDIATE);
        intermediateIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(intermediateBroadcastReceiver, intermediateIntentFilter);

        IntentFilter finishIntentFilter = new IntentFilter(STATUS_FINISH);
        finishIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(finishBroadcastReceiver, finishIntentFilter);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startService();
        }
    };

    public void startService() {
        firstIntentService = new Intent(this, FirstIntentService.class);
        int time = Integer.parseInt(textViewTime.getText().toString());
        startService(firstIntentService.putExtra(PARAM_TIME, time).putExtra(PARAM_TASK,"Кофе сварен."));
        startService(firstIntentService.putExtra(PARAM_TIME, time+1).putExtra(PARAM_TASK,"Каша готова."));
        startService(firstIntentService.putExtra(PARAM_TIME, time+2).putExtra(PARAM_TASK,"Молоко налито."));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewTime.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(startBroadcastReceiver);
        unregisterReceiver(intermediateBroadcastReceiver);
        unregisterReceiver(finishBroadcastReceiver);
        if (firstIntentService != null) {
            stopService(firstIntentService);
            firstIntentService = null;
        }
    }

    public class StartBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            textViewInfo.setText("Время пошло...");
        }
    }
    public class IntermediateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(PARAM_RESULT, 0);
            textViewResult.setText("Прошло: " + result + " с");
        }
    }

    public class FinishBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String task = intent.getStringExtra(PARAM_TASK);
            textViewInfo.setText(task);
        }
    }
}

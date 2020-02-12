package com.jtmcompany.waist_guard_project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class sensor_info extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private final String BROADCAST_MESSAGE_DISCONNECT="com.jtmcompany.waist_guard_project.connect";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);
        final TextView heart_text=findViewById(R.id.heart_text);
        final TextView temp_text=findViewById(R.id.temp_text);
        final TextView fall_text=findViewById(R.id.fall_text);
        final ProgressBar heart_progress=findViewById(R.id.heart_progress);
        final ProgressBar temp_progress=findViewById(R.id.temp_progress);


        registerReceiver();

        //지속적으로 UI갱신을위해 스레드실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted())
                {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences auto=getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                String mfall=auto.getString("fall",null);
                                int mtemp=auto.getInt("temp",0);
                                int mheart=auto.getInt("heart",0);
                                heart_progress.setProgress(mheart);
                                heart_text.setText(Integer.toString(mheart));
                                temp_progress.setProgress(mtemp);
                                temp_text.setText(Integer.toString(mtemp));
                                fall_text.setText(mfall);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //브로드캐스트리시버등록
    //포그라운드서비스에서 연결이 끊어지면 메시지보냄-> 메인엑티비티로돌아감
    private void registerReceiver(){
        if(mReceiver !=null) return;

        final IntentFilter mFilter= new IntentFilter();
        mFilter.addAction(BROADCAST_MESSAGE_DISCONNECT);

        mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(BROADCAST_MESSAGE_DISCONNECT)){
                    Intent sensorintent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(sensorintent);

                    finish();
                }
            }
        };
        registerReceiver(mReceiver,mFilter);
    }

    private void unregisterReceiver(){
        if(mReceiver !=null){
            unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}

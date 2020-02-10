package com.jtmcompany.waist_guard_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class sensor_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);
        final TextView heart_text=findViewById(R.id.heart_text);
        final TextView temp_text=findViewById(R.id.temp_text);
        final TextView fall_text=findViewById(R.id.fall_text);
        final ProgressBar heart_progress=findViewById(R.id.heart_progress);
        final ProgressBar temp_progress=findViewById(R.id.temp_progress);

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
}

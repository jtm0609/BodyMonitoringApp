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
        TextView heart_text=findViewById(R.id.heart_text);
        TextView temp_text=findViewById(R.id.temp_text);
        TextView fall_text=findViewById(R.id.fall_text);
        ProgressBar heart_progress=findViewById(R.id.heart_progress);
        ProgressBar temp_progress=findViewById(R.id.temp_progress);

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
}

package com.jtmcompany.waist_guard_project.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.sensor_info;


public class body_Info extends Fragment {


    TextView heart_text;
    TextView temp_text;
    TextView fall_text;
    ProgressBar heart_progress;
    ProgressBar temp_progress;

    public body_Info() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_body__info, container, false);
        heart_text = rootView.findViewById(R.id.heart_text);
        temp_text = rootView.findViewById(R.id.temp_text);
        fall_text = rootView.findViewById(R.id.fall_text);
        heart_progress = rootView.findViewById(R.id.heart_progress);
        temp_progress = rootView.findViewById(R.id.temp_progress);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final sensor_info activity = (sensor_info) getActivity();
        //지속적으로 UI갱신을위해 스레드실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(1000);
                        //화면전환되었을때 스레드중지(컨텍스트가 NULL일때)
                        //컨텍스트는 OnAttach()에서 엑티비티의 컨텍스트를받고, onDetach()를하고 컨텍스트는 NULL이됨
                        if (getContext() == null) {
                            Log.d("TAKMIN", "NULL");
                            return;
                        } else
                            Log.d("TAKMIN", "NO");

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences auto = activity.getSharedPreferences("auto", activity.MODE_PRIVATE);
                                String mfall = auto.getString("fall", null);
                                int mtemp = auto.getInt("temp", 0);
                                int mheart = auto.getInt("heart", 0);
                                Log.d("TAKMIN", "TEST: " + mheart);
                                heart_progress.setProgress(mheart);
                                heart_text.setText(Integer.toString(mheart));
                                temp_progress.setProgress(mtemp);
                                temp_text.setText(Integer.toString(mtemp));
                                fall_text.setText(mfall);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("TAKMIN", "Interrupt");
                    } catch (NullPointerException e) {
                        Log.d("TAKMIN", "에러");
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAKMIN", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAKMIN", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TAKMIN", "onDetach");

    }
}

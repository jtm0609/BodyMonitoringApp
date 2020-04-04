package com.jtmcompany.waist_guard_project.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.jtmcompany.waist_guard_project.Fragment.body_Info;
import com.jtmcompany.waist_guard_project.Fragment.friend_info;
import com.jtmcompany.waist_guard_project.Fragment.notification_info;
import com.jtmcompany.waist_guard_project.R;

public class sensor_info extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private final String BROADCAST_MESSAGE_DISCONNECT="com.jtmcompany.waist_guard_project.connect";
    body_Info fragment1;
    friend_info fragment2;
    notification_info fragmnet3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);

        //브로드캐스트등록
        registerReceiver();

        //프레그먼트 정의
        fragment1=new body_Info();
        fragment2=new friend_info();
        fragmnet3=new notification_info();

        //처음엑티비티가켜지면 프래그먼트1을 화면에띄움
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();

        //탭을눌렀을때 그에맞는 프래그먼트가 디스플레이
        TabLayout tabs=findViewById(R.id.tab);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();
                Fragment selected=null;
                if(position==0){
                    selected=fragment1;
                }else if(position==1){
                    selected=fragment2;
                }else if(position==2) {
                    selected=fragmnet3;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


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

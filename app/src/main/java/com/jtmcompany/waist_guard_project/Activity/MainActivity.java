package com.jtmcompany.waist_guard_project.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jtmcompany.waist_guard_project.Adapter.MyPagerAdapter;
import com.jtmcompany.waist_guard_project.R;

//센서정보, 친구정보, 알림정보 나타내는 메인 엑티비티
public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private final String BROADCAST_MESSAGE_DISCONNECT="com.jtmcompany.waist_guard_project.connect";
    private ViewPager viewPager;
    private boolean isGuardian=false;
    private Toolbar toolbar;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);
        init();
        initSet();
        registerReceiver(); //브로드캐스트등록
    }

    public void init(){
        tabs=findViewById(R.id.tab); //탭레이아웃 정의
        viewPager=findViewById(R.id.container); //뷰페이저 정의
        toolbar=findViewById(R.id.sensorToolbar); //툴바 설정

        //사용자정보가져오기: 보호자라면 true 사용자라면 false
        SharedPreferences sharedPref=getSharedPreferences("user", Activity.MODE_PRIVATE);
        isGuardian=sharedPref.getBoolean("isGuardian",false);
    }
    public void initSet(){
        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //뷰페이저 - 텝레이아웃 연결
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),isGuardian));
        tabs.setupWithViewPager(viewPager);

    }


    //브로드캐스트리시버등록
    //포그라운드서비스에서 블루투스 연결이 끊어지면 메시지보냄-> 메인엑티비티로돌아감
    private void registerReceiver(){
        if(mReceiver !=null) return;
        final IntentFilter mFilter= new IntentFilter();
        mFilter.addAction(BROADCAST_MESSAGE_DISCONNECT);
        mReceiver=new BroadcastReceiver() {
            @Override
                public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(BROADCAST_MESSAGE_DISCONNECT)){
                    Intent sensorintent = new Intent(getApplicationContext(), UserServiceActivity.class);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout_button) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();

            //블루투스 연결해제 알림
            SharedPreferences auto = getSharedPreferences("bt_connect?", Activity.MODE_PRIVATE);
            SharedPreferences.Editor auto_convert = auto.edit();
            auto_convert.putBoolean("mconnected", false);
            auto_convert.commit();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}

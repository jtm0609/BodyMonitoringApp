package com.jtmcompany.waist_guard_project.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jtmcompany.waist_guard_project.Fragment.body_Info;
import com.jtmcompany.waist_guard_project.Fragment.friend_info;
import com.jtmcompany.waist_guard_project.Fragment.notification_info;
import com.jtmcompany.waist_guard_project.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private final String BROADCAST_MESSAGE_DISCONNECT="com.jtmcompany.waist_guard_project.connect";
    ViewPager viewPager;
    boolean isGuardian=false;
    Toolbar toolbar;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);


        toolbar=findViewById(R.id.sensorToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //보호자라면 true 사용자라면 false
        SharedPreferences sharedPref=getSharedPreferences("user", Activity.MODE_PRIVATE);
        isGuardian=sharedPref.getBoolean("isGuardian",false);
        Log.d("tak5","test: "+isGuardian);


        //브로드캐스트등록
        registerReceiver();

        //탭레이아웃정의
        TabLayout tabs=findViewById(R.id.tab);

        //뷰페이저 정의 & 탭레이아웃과 연결
        viewPager=findViewById(R.id.container);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);

    }

    //뷰페이저 어댑터
    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments=new ArrayList<>();
        List<String> titles_list=new ArrayList<>();
        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);

            //사용자라면
            if(!isGuardian) {
                titles_list.add("생체정보");
                titles_list.add("친구목록");
                titles_list.add("알림");

                fragments.add(new body_Info());
                fragments.add(new friend_info());
                fragments.add(new notification_info());
            }
            //보호자라면
            else if(isGuardian){

                titles_list.add("친구목록");
                titles_list.add("알림");

                fragments.add(new friend_info());
                fragments.add(new notification_info());
            }

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return titles_list.get(position);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}

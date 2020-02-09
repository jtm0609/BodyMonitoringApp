package com.jtmcompany.waist_guard_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {
    private BluetoothSPP bt;
    private Foreground_Service mservice;
    private boolean mbound;

    //서비스 바인드를 위한 메소드
    private ServiceConnection mConnection=new ServiceConnection() {
        //바인드되면 포그라운드서비스 시작시킴
        //바인드되면 service=mbinder가됨
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Foreground_Service.Mybinder binder= (Foreground_Service.Mybinder)service;
            mservice=binder.getService();
            mbound= true;

            Intent mIntent = new Intent(getApplicationContext(), Foreground_Service.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mIntent);
            }
            else {
                startService(mIntent);
            }

            //서비스 시작시 ,블루투스관련코드
            bt = mservice.getBlueToothSPP();
            if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
                Toast.makeText(getApplicationContext()
                        , "Bluetooth is not available"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }

            if (!bt.isBluetoothEnabled()) { //블루투스가 꺼져있다면 키도록 액션요청
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
            } else {
                if (!bt.isServiceAvailable()) {
                    bt.setupService();
                    bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                }
            }
        }

        //예기치않은 상황 kill됬을때 호출됨
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mservice=null;
            mbound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //서비스생성 & 바인드
        bindService(new Intent(this, Foreground_Service.class), mConnection,BIND_AUTO_CREATE);

        Button btnConnect = findViewById(R.id.conn_button); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mservice.getBlueToothSPP().getServiceState() == BluetoothState.STATE_CONNECTED) { //연결이되어있다면
                    mservice.getBlueToothSPP().disconnect(); //연결끊음
                } else { //연결이 안되있다면
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

    }


    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(mbound){
            unbindService(mConnection);
            mbound=false;
        }

         */
    }

    public void onDestroy() {
        super.onDestroy();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                mservice.getBlueToothSPP().connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) { //블루투스 활성화? OK버튼을눌렀을때
                mservice.getBlueToothSPP().setupService();
                mservice.getBlueToothSPP().startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}





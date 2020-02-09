package com.jtmcompany.waist_guard_project;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class Foreground_Service extends Service {
    private BluetoothSPP bt;
    private  IBinder mBinder = new Foreground_Service.Mybinder();
    private  boolean mconnected=false;
    private NotificationManager mNotificationManager;

    public class Mybinder extends Binder {
        public Foreground_Service getService(){
            return Foreground_Service.this;
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /*
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel("default", name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

         */
        bt= new BluetoothSPP(this);

        //서비스에서 블루투스연결관리
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) { //연결됐을 때
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                //연결됨
                mconnected=true;


            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                //연결끊어짐
                mconnected=false;
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        //서비스로 데이터 실시간으로 수신
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                String[] s = message.split("/");
                int temp = Integer.parseInt(s[0]);
                int vibration = Integer.parseInt(s[1]);
                int pulse=Integer.parseInt(s[2]);

                //실시간으로 온도, 충격 쉐어드프리퍼런스에저장
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putInt("temp", temp);
                autoLogin.putInt("heart",pulse);
                autoLogin.commit();
                //충격검사
                if (vibration == 1) {
                    autoLogin.putString("fall", "감지함");
                    autoLogin.commit();
                } else {
                    autoLogin.putString("fall", "감지못함");
                    autoLogin.commit();
                }
                //if(vibration==1 && temp>=37)
            }
        });
        //Service가 강제 종료되었을 경우 시스템이 다시 Service를 재시작
        return START_STICKY;
    }

    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행중");

        Intent notificationIntent=new Intent(this,MainActivity.class);
        //알림을 눌렀을때 대기하고있던 인텐트가 실행
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(pendingIntent);

        //안드로이드 오레오이상에서는 다음과같은 노티피케이션 채널을 등록해야함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1,builder.build());
    }

    //블루투스SPP객체 리턴
    public BluetoothSPP getBlueToothSPP(){
        return bt;
    }

    //블루투스가 연결되있는지확인 메소드
    public boolean isBluetoothConnected(){
        return mconnected;
    }

    //서비스중지
    public void stopService(){
        stopForeground(true);
        stopSelf();
    }

}


package com.jtmcompany.waist_guard_project.Service;

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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jtmcompany.waist_guard_project.Activity.UserServiceActivity;
import com.jtmcompany.waist_guard_project.R;

import java.util.HashMap;
import java.util.Map;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class Foreground_Service extends Service {
    private BluetoothSPP bt;
    private  IBinder mBinder = new Foreground_Service.Mybinder();
    public boolean mconnected=false;
    private NotificationManager mNotificationManager;
    private final String BROADCAST_MESSAGE_CONNECT="com.jtmcompany.waist_guard_project.connect";
    private final String BROADCAST_MESSAGE_DISCONNECT="com.jtmcompany.waist_guard_project.connect";
    DatabaseReference mDatabasae= FirebaseDatabase.getInstance().getReference();
    String myUid= FirebaseAuth.getInstance().getUid();
    Map<String,Object> sensorInfo_map;


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

    //서비스종료시점
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("tak","서비스종료");
        //블루투스 연결해제 알림
        SharedPreferences auto = getSharedPreferences("bt_connect?", Activity.MODE_PRIVATE);
        SharedPreferences.Editor auto_convert = auto.edit();
        auto_convert.putBoolean("mconnected", false);
        auto_convert.commit();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //센서정보 수집할때 map사용
        sensorInfo_map= new HashMap<>();
        bt= new BluetoothSPP(this);


        //서비스에서 블루투스연결관리
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) { //연결됐을 때
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                //연결됨
                //블루투스가연결되있으면 자동으로 화면전환하게 하기위해 SharedPreference 사용
                mconnected=true;
                SharedPreferences auto = getSharedPreferences("bt_connect?", Activity.MODE_PRIVATE);
                SharedPreferences.Editor auto_convert = auto.edit();
                auto_convert.putBoolean("mconnected", mconnected);
                auto_convert.commit();


                //브로드캐스트메시지전송
                Intent intent = new Intent(BROADCAST_MESSAGE_CONNECT);
                sendBroadcast(intent);
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                //연결끊어짐
                //SharedPreference 사용
                mconnected=false;
                SharedPreferences auto = getSharedPreferences("bt_connect?", Activity.MODE_PRIVATE);
                SharedPreferences.Editor auto_convert = auto.edit();
                auto_convert.putBoolean("mconnected", mconnected);
                auto_convert.commit();

                Intent intent = new Intent(BROADCAST_MESSAGE_DISCONNECT);
                sendBroadcast(intent);
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        startForegroundService();


        //테스트
        sensorInfo_map.put("pulse","65");
        sensorInfo_map.put("temp","36");
        sensorInfo_map.put("vibration","감지못함");
        mDatabasae.child("유저").child("사용자").child(myUid).child("sensorInfo").setValue(sensorInfo_map);
        //*테스트

        //서비스로 데이터 실시간으로 수신
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.d("tak","연결된 디바이스: "+bt.getConnectedDeviceName());
                SharedPreferences auto = getSharedPreferences("bt_connect?", Activity.MODE_PRIVATE);
                if(auto.getBoolean("mconnected", false)==false) bt.disconnect();

                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                String[] s = message.split("/");
                String pulse=s[0];
                String temp = s[1];
                int vibration = Integer.parseInt(s[2]);


                //실시간으로 온도, 충격 쉐어드프리퍼런스에저장
                sensorInfo_map.put("pulse",pulse);
                sensorInfo_map.put("temp",temp);


                /*
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("temp", temp);
                autoLogin.putString("heart",pulse);
                autoLogin.commit();

                 */
                //충격검사
                if (vibration == 1) {
                    sensorInfo_map.put("vibration","감지함");
                    //autoLogin.putString("fall", "감지함");
                    //autoLogin.commit();
                } else {
                    sensorInfo_map.put("vibration","감지못함");
                    //autoLogin.putString("fall", "감지못함");
                    //autoLogin.commit();
                }


                mDatabasae.child("유저").child("사용자").child(myUid).child("sensorInfo").updateChildren(sensorInfo_map);

            }
        });
        //Service가 강제 종료되었을 경우 시스템이 다시 Service를 재시작
        return START_NOT_STICKY;
    }

    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행중");

        Intent notificationIntent=new Intent(this, UserServiceActivity.class);
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


package com.jtmcompany.waist_guard_project.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.Adapter.Recommend_FriendAdapter;
import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecommendFriendActivity extends AppCompatActivity implements Recommend_FriendAdapter.MyRecyclerViewClickListener {
    List<User> friend_Datas=new ArrayList<>();
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private String myUid= FirebaseAuth.getInstance().getUid();
    private RecyclerView recyclerView;
    private final String FCM_MESSAGE_URL="https://fcm.googleapis.com/fcm/send";
    private final String SEVER_KEY="AAAAdOcJLmA:APA91bEl0w-VPDhYIiEGGmuZTmKAfvPFuU2QHM_ozx9MmCuwJZh2O4VsRKvU4lUz8hihHoIKV5r5DYvth9_MaxXIpPIQjLwbAZpqfs7m7ZhVGh6BZMYxdsNOLLez3o26G9evrb-AHvgr";
    String user_kinds;
    String object_user_kinds;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friend_info);
        Toolbar toolbar=findViewById(R.id.recommendFriendToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView=findViewById(R.id.recycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final Recommend_FriendAdapter adapter=new Recommend_FriendAdapter();

        //어댑터리스너설정
        adapter.setOnClickListener(this);




        //전화번호를 가져오기 위해 contentResolver를 사용. ->context()필요
        //전화번호 데이터를 가져오는 커넥터 역할.
        ContentResolver resolver=getContentResolver();

        //데이터가 있는 테이블 주소가져오기
        Uri phoneUri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //테이블에서 가져올 컬럼명을 정의
        String proj[]={ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        //컨텐트 리졸버로 데이터 가져오기. 가져온형태 ->커서
        Cursor cursor= resolver.query(phoneUri, proj,null,null,null);

        //cursor에 데이터 존재여부
        if(cursor !=null){
            while(cursor.moveToNext()){
                int index=cursor.getColumnIndex(proj[0]);
                int id=cursor.getInt(index);

                index=cursor.getColumnIndex(proj[1]);
                String name=cursor.getString(index);
                //테스트
                Log.d("TTT","이름: "+name);

                index=cursor.getColumnIndex(proj[2]);
                String tel=cursor.getString(index);
                //예외처리
                if(tel.startsWith("+82"))
                    tel=tel.replace("+82","0");
                tel=tel.replace("-","");
                //예외처리끝

                //테스트
                Log.d("TTT","폰: "+tel);

                User friend_Data=new User(name,tel);

                friend_Datas.add(friend_Data);
                //리스트에 열 하나하나의 데이터 클래스가 저장됨 -> 주소록만큼 리스트개수가 생성됨
            }
        }
        Log.d("TTT","test:" + friend_Datas.size());


        cursor.close(); //닫지않으면 계속열려있음

        //자신이 보호자면 user_kinds=보호자, 상대방은 object_user_kinds=사용자
        SharedPreferences sharedPref=getSharedPreferences("user", Activity.MODE_PRIVATE);
        boolean isGuardian=sharedPref.getBoolean("isGuardian",false);
        if(isGuardian) {
            user_kinds="보호자";
            object_user_kinds="사용자";
        }
        else {
            user_kinds="사용자";
            object_user_kinds="보호자";
        }

    //DB에 데이터읽어어와서 전화번호와 휴대폰 연락처의 전화번호와 일치하면
    mDatabase.child("유저").child(object_user_kinds).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("Token",FirebaseAuth.getInstance().getUid());
            for(DataSnapshot data: dataSnapshot.getChildren()){
                User friendUser=data.getValue(User.class);
                for(int i=0; i<friend_Datas.size(); i++){
                    if(friend_Datas.get(i).getPhoneNumber().equals(friendUser.getPhoneNumber())){
                        Log.d("TRUE","TRUE");
                        adapter.addItem(new User(friendUser.getName(), friendUser.getPhoneNumber()));
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        Log.d("TRUE","FALSE");
                    }
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    });

    }

    //친구신청버튼을 누르면 그사람 Uid를 참조해서 토큰을 얻어오고 토큰을 이용해서 sendPostToFcm메소드호출
    @Override
    public void onButtonClicked(int position, final String name) {

        Toast.makeText(this, "버튼"+position, Toast.LENGTH_SHORT).show();
        mDatabase.child("유저").child(user_kinds).child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User send_User=dataSnapshot.getValue(User.class);
                final String send_name=send_User.getName();
                Log.d("TAK2","TEST: "+send_User.getPhoneNumber()+" "+send_User.getName());

                mDatabase.child("유저").child(object_user_kinds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            final User receive_user=data.getValue(User.class);
                            String receive_name=receive_user.getName();
                            if (name.equals(receive_name)) {
                                Log.d("test3","name:"+ name);
                                Log.d("test3","receivename:"+ receive_name);
                                Log.d("test3","receveUid:"+ receive_user.getUserUid());
                                String receive_User_Uid =receive_user.getUserUid();
                                Log.d("Token","accept_User_Uid: "+ data.getKey());
                                sendPostToFCM(send_name+"님이 친구요청을 보냈습니다.",receive_User_Uid);

                                //DB에 업데이트(친구요청이 누구에게보내는지, 누구로부터받는지가 저장)
                                mDatabase.child("유저").child(user_kinds).child(send_User.getUserUid()).child("sendToRequest").updateChildren(receive_user.toMap());
                                mDatabase.child("유저").child(object_user_kinds).child(receive_user.getUserUid()).child("receiveToRequest").updateChildren(send_User.toMap());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //http통신을이용하여 파이어베이스서버키와 받아온토큰을 통해 상대방에게 FCM전송
    public void sendPostToFCM(final String message, String Uid){
        mDatabase.child("유저").child(object_user_kinds).child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final User user=dataSnapshot.getValue(User.class);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                JSONObject root = new JSONObject();
                                JSONObject notification =new JSONObject();
                                notification.put("title","친구요청");
                                notification.put("body",message);
                                root.put("notification",notification);
                                root.put("to",user.getFcmToken());

                                URL Url=new URL(FCM_MESSAGE_URL);
                                HttpURLConnection conn=(HttpURLConnection)Url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);
                                //API 키는 호출하는 프로젝트, 즉 API를 호출하는 애플리케이션이나 사이트를 식별합니다.
                                //인증 토큰은 사용자, 즉 앱이나 사이트를 사용하고 있는 사람을 식별합니다.
                                conn.addRequestProperty("Authorization","key=" + SEVER_KEY); //서버키가 필요한이유: 서버에서 어떤프로젝트의 서비스요청인지 확인하기위해 필요
                                conn.setRequestProperty("Accept","application/json"); //서버 Response Data를 JSON 형식의 타입으로 요청.
                                conn.setRequestProperty("Content-type","application/json"); //Request Body(데이터) 전달시 application/json로 서버에 전달

                                OutputStream os = conn.getOutputStream();
                                os.write(root.toString().getBytes("utf-8"));
                                os.flush();
                                conn.getResponseCode();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }




}

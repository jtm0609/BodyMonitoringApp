package com.jtmcompany.waist_guard_project.Activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.RecrclerView.Recommend_FriendAdapter;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class recommend_friend_info extends AppCompatActivity implements Recommend_FriendAdapter.MyRecyclerViewClickListener {
    List<User> friend_Datas=new ArrayList<>();
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private String Uid= FirebaseAuth.getInstance().getUid();
    private RecyclerView recyclerView;
    private final String FCM_MESSAGE_URL="https://fcm.googleapis.com/fcm/send";
    private final String SEVER_KEY="AAAAdOcJLmA:APA91bEl0w-VPDhYIiEGGmuZTmKAfvPFuU2QHM_ozx9MmCuwJZh2O4VsRKvU4lUz8hihHoIKV5r5DYvth9_MaxXIpPIQjLwbAZpqfs7m7ZhVGh6BZMYxdsNOLLez3o26G9evrb-AHvgr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friend_info);
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

        //DB에 데이터읽어어와서 이름과 휴대폰 연락처를불러온이름과 일치하면
    mDatabase.child("유저").child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("Token",FirebaseAuth.getInstance().getUid());
            for(DataSnapshot data: dataSnapshot.getChildren()){
                User friendUser=data.getValue(User.class);
                for(int i=0; i<friend_Datas.size(); i++){
                    if(friend_Datas.get(i).getName().equals(friendUser.getName())){
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
        mDatabase.child("유저").child("사용자").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User send_User=dataSnapshot.getValue(User.class);
                final String send_name=send_User.getName();
                Log.d("TAK2","TEST: "+send_User.getPhoneNumber()+" "+send_User.getName());

                mDatabase.child("유저").child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            final User receive_user=data.getValue(User.class);
                            String receive_name=receive_user.getName();
                            if (name.equals(receive_name)) {
                                String receive_User_Uid =receive_user.getUserUid();
                                Log.d("Token","accept_User_Uid: "+ data.getKey());
                                sendPostToFCM(send_name+"님이 친구요청을 보냈습니다.",receive_User_Uid);

                                //DB에 업데이트(누구에게보내는지, 누구로부터받는지가 저장)
                                mDatabase.child("유저").child("사용자").child(send_User.getUserUid()).child("sendToRequest").updateChildren(receive_user.toMap());
                                mDatabase.child("유저").child("사용자").child(receive_user.getUserUid()).child("receiveToRequest").updateChildren(send_User.toMap());
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
        mDatabase.child("유저").child("사용자").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                conn.addRequestProperty("Authorization","key=" + SEVER_KEY);
                                conn.setRequestProperty("Accept","application/json");
                                conn.setRequestProperty("Content-type","application/json");
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

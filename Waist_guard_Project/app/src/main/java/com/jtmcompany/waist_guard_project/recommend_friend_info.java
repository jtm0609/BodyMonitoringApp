package com.jtmcompany.waist_guard_project;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.RecrclerView.Recommend_FriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class recommend_friend_info extends AppCompatActivity {
    List<User> friend_Datas=new ArrayList<>();
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friend_info);
        recyclerView=findViewById(R.id.recycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final Recommend_FriendAdapter adapter=new Recommend_FriendAdapter();






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

    mDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            DataSnapshot mDataSnapshot=dataSnapshot.child("유저").child("사용자");
                Log.d("TAKK","개수: "+mDataSnapshot.getChildrenCount());
                for(DataSnapshot snapshot: mDataSnapshot.getChildren()) {
                    Log.d("TAKK", "목록 :" + snapshot.getKey());
                    Log.d("TAKK", "목록 :" + snapshot.getValue());

                    //연락처에있는 이름과 데이터베이스에있는 이름이같다면 이름과 연락처를 리싸이클러뷰에추가
                    for(int i=0; i<friend_Datas.size(); i++){
                        if(friend_Datas.get(i).getName().equals(snapshot.getKey())) {
                            Log.d("TRUE","TRUE");
                            adapter.addItem(new User(snapshot.getKey(), (String) snapshot.getValue()));
                            recyclerView.setAdapter(adapter);

                        }
                        else{
                            Log.d("TRUE","FALSE");
                        }
                    }
                }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    }


}

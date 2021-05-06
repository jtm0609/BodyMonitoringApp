package com.jtmcompany.waist_guard_project.Fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.Adapter.NotiRecyclerAdapter;
import com.jtmcompany.waist_guard_project.R;

import java.util.HashMap;
import java.util.Map;

public class NotiInfoFragment extends Fragment implements NotiRecyclerAdapter.NotiRecyclerViewClcikListener {

    //친구가 친구신청을 보내면 이 프레그먼트에서 알림이나옴

DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
String MyUid= FirebaseAuth.getInstance().getUid();
String Myname;
String object_user_kinds;
String user_kinds;
    public NotiInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup)inflater.inflate(R.layout.fragment_notification_info, container,false);
        final RecyclerView recyclerView=rootView.findViewById(R.id.notiRecycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final NotiRecyclerAdapter adapter=new NotiRecyclerAdapter();
        adapter.setOnClickListener(this);

        //자신이 보호자면 user_kinds=보호자, 상대방은 object_user_kinds=사용자
        SharedPreferences sharedPref=getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);

        boolean isGuardian=sharedPref.getBoolean("isGuardian",false);
        if(isGuardian) {
            user_kinds="보호자";
            object_user_kinds="사용자";
        }
        else {
            user_kinds="사용자";
            object_user_kinds="보호자";
        }

        mdatabase.child("유저").child(user_kinds).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //아이템클릭에(OnButtonClicked) 사용하기위해 자신의이름을 변수에 저장
                Myname=(String)dataSnapshot.child(MyUid).child("name").getValue();


                //DB에저장된 내가 받은 친구요청리스트에있는 UID를 읽어아서 요청을 보낸사람들의 리스트를 리싸이클러뷰에 보여줌
                Iterable<DataSnapshot> send_Users=dataSnapshot.child(MyUid).child("receiveToRequest").getChildren();
                for(DataSnapshot send_User: send_Users){
                    final String send_Users_Uid=(String)send_User.getValue();
                    Log.d("TAAK","TEST2: "+send_Users_Uid);
                    mdatabase.child("유저").child(object_user_kinds).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String send_Name =(String)dataSnapshot.child(send_Users_Uid).child("name").getValue();
                            Log.d("TAAK","TEST: "+send_Name);
                            adapter.addItem(send_Name);
                            adapter.addItemHash(send_Name,send_Users_Uid); //해쉬값에도 저장
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Log.d("TAAK","TEST: "+ adapter.getItemCount());

        return rootView;
    }


    //리싸이클러뷰에 있는 아이템의 친구수락버튼을 누르면 각유저 DB의 friend차일드에 추가됨
    @Override
    public void onAcceptBtClicked(int position, String name,String uid) {
       Log.d("tak",name+position);

        //내 친구목록창입장
        Map<String,Object> yourUid_Name=new HashMap<>();
        yourUid_Name.put(name,uid);
        mdatabase.child("유저").child(user_kinds).child(MyUid).child("friend").updateChildren(yourUid_Name);

        //상대방 친구목록창입장
        Map<String,Object> myUid_Name=new HashMap<>();
        myUid_Name.put(Myname,MyUid);
        mdatabase.child("유저").child(object_user_kinds).child(uid).child("friend").updateChildren(myUid_Name);

    }

    //친구거절
    @Override
    public void onDenyBtClicked(int position, String name, String Uid) {
        //받은 친구요청 목록에서 제거
        mdatabase.child("유저").child(user_kinds).child(MyUid).child("receiveToRequest").child(name).removeValue();
    }
}

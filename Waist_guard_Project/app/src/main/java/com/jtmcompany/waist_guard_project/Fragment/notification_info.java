package com.jtmcompany.waist_guard_project.Fragment;


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
import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.RecrclerView.NotiAdapter;

import java.util.HashMap;
import java.util.Map;

public class notification_info extends Fragment implements NotiAdapter.NotiRecyclerViewClcikListener {
DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
String MyUid= FirebaseAuth.getInstance().getUid();
String Myname;
    public notification_info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup)inflater.inflate(R.layout.fragment_notification_info, container,false);
        final RecyclerView recyclerView=rootView.findViewById(R.id.notiRecycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final NotiAdapter adapter=new NotiAdapter();
        adapter.setOnClickListener(this);

        mdatabase.child("유저").child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //아이템클릭에(OnButtonClicked) 사용하기위해 자신의이름을 변수에 저장
                Myname=(String)dataSnapshot.child(MyUid).child("name").getValue();


                Iterable<DataSnapshot> send_Users=dataSnapshot.child(MyUid).child("receiveToRequest").getChildren();
                for(DataSnapshot send_User: send_Users){
                    final String send_Users_Uid=send_User.getKey();
                    Log.d("TAAK","TEST: "+send_Users_Uid);
                    mdatabase.child("유저").child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void onButtonClicked(int position, String name,String uid) {
       Log.d("tak",name+position);

        //내 친구목록창입장
        Map<String,Object> YourUid_Name=new HashMap<>();
        YourUid_Name.put(name,uid);
        mdatabase.child("유저").child("사용자").child(MyUid).child("friend").updateChildren(YourUid_Name);

        //상대방 친구목록창입장
        Map<String,Object> MyUid_Name=new HashMap<>();
        MyUid_Name.put(Myname,MyUid);
        mdatabase.child("유저").child("사용자").child(uid).child("friend").updateChildren(MyUid_Name);

    }
}

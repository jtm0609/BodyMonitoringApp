package com.jtmcompany.waist_guard_project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.Activity.friend_sensor_info;
import com.jtmcompany.waist_guard_project.Activity.recommend_friend_info;
import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.RecrclerView.FriendAdapter;

public class friend_info extends Fragment implements FriendAdapter.friendRecyclerListener{
    DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
    String myUid= FirebaseAuth.getInstance().getUid();


    public friend_info() {
        // Required empty public constructor
    }

//addItem->setAdapter하면
//UserAdapter클래스에서 onCreateViewHolder->onBindViewHolder->setItem
//아이템 하나당 onCreateViewHolder->ViewHolder->onBindVieHolder->ViewHolder->setItem 반복
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_friend_info, container, false);
        RecyclerView recyclerView= rootView.findViewById(R.id.recycler);
       FloatingActionButton Friend_Add_Bt=rootView.findViewById(R.id.friend_add_bt);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final FriendAdapter adapter= new FriendAdapter();
        adapter.setOnclickListener(this);

        //DB friend의 차일드들의 리스트를 프레그먼트에 띄움
        mdatabase.child("유저").child("사용자").child(myUid).child("friend").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Log.d("taktak","test: "+ data);
                    String name=data.getKey();
                    String uid=(String)data.getValue();
                    Log.d("tak33","test2: "+ uid);
                    adapter.addItemHash(name,uid);
                    adapter.addItem(new User(name));
                    //파베는 비동기로작동하기때문에 다음코드를 넣어야함
                    // 리사이클러뷰가 어댑터등록되는시점보다 파베내용을 addItem하는시점이 더늦음
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        recyclerView.setAdapter(adapter);
        Log.d("TAK","TEST: "+"setAdapter");

        //친구추가 버튼을눌렀을때,
        Friend_Add_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), recommend_friend_info.class);
                startActivity(intent);

            }
        }
        );
        return rootView;
    }


    @Override
    public void onButtonClicked(int position, String name, String uid) {
        Log.d("tak33","test: "+position+ name+ uid);
        Intent intent = new Intent(getActivity(), friend_sensor_info.class);
        intent.putExtra("name",name);
        intent.putExtra("uid",uid);
        startActivity(intent);

    }
}



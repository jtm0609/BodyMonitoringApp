package com.jtmcompany.waist_guard_project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.RecrclerView.FriendAdapter;
import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.recommend_friend_info;

public class friend_info extends Fragment {


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
        Button Friend_Add_Bt=rootView.findViewById(R.id.friend_add_bt);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        FriendAdapter adapter= new FriendAdapter();
        adapter.addItem(new User("김민수","010-1000-1000"));
        Log.d("TAK","TEST: "+"addItem1");
        adapter.addItem(new User("홍길동","010-3333-3333"));
        Log.d("TAK","TEST: "+"addItem2");
        adapter.addItem(new User("홍길동","010-3333-3333"));
        Log.d("TAK","TEST: "+"addItem3");

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



    }



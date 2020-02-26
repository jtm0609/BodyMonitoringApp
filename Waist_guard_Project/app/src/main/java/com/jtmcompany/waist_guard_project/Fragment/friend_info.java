package com.jtmcompany.waist_guard_project.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.waist_guard_project.R;
import com.jtmcompany.waist_guard_project.RecrclerView.UserAdapter;
import com.jtmcompany.waist_guard_project.User;

public class friend_info extends Fragment {


    public friend_info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_friend_info, container, false);
        RecyclerView recyclerView= rootView.findViewById(R.id.recycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        UserAdapter adapter= new UserAdapter();
        adapter.addItem(new User("김민수","010-1000-1000"));
        adapter.addItem(new User("홍길동","010-3333-3333"));
        adapter.addItem(new User("홍길동","010-3333-3333"));
        Log.d("TAK","TEST: "+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}

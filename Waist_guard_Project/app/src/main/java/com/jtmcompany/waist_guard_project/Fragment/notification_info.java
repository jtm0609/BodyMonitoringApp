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

public class notification_info extends Fragment {
DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
String Uid= FirebaseAuth.getInstance().getUid();
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

        mdatabase.child("유저").child("사용자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> send_Users=dataSnapshot.child(Uid).child("receiveToRequest").getChildren();
                for(DataSnapshot send_User: send_Users){
                    final String send_Users_Uid=send_User.getKey();
                    Log.d("TAAK","TEST: "+send_Users_Uid);
                    mdatabase.child("유저").child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String send_Name =(String)dataSnapshot.child(send_Users_Uid).child("name").getValue();
                            Log.d("TAAK","TEST: "+send_Name);
                            adapter.addItem(send_Name);
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

}

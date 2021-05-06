package com.jtmcompany.waist_guard_project.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.Activity.MainActivity;
import com.jtmcompany.waist_guard_project.Model.User;
import com.jtmcompany.waist_guard_project.R;


public class BodyInfoFragment extends Fragment {

    TextView user_name_text;
    TextView heart_text;
    TextView temp_text;
    TextView fall_text;
    ProgressBar heart_progress;
    ProgressBar temp_progress;


    public BodyInfoFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_body__info, container, false);
        user_name_text=rootView.findViewById(R.id.user_name);
        heart_text = rootView.findViewById(R.id.heart_text);
        temp_text = rootView.findViewById(R.id.temp_text);
        fall_text = rootView.findViewById(R.id.fall_text);
        heart_progress = rootView.findViewById(R.id.heart_progress);
        temp_progress = rootView.findViewById(R.id.temp_progress);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();

        //현재 Uid와일치하는 유저클래스의 이름 데이터읽어오기
        String myUid= FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase;
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("유저").child("사용자").child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user=dataSnapshot.getValue(User.class);
                Log.d("TAK2","User:"+ user);
                final String Name=user.getName();
                Log.d("TAK2",Name);
                user_name_text.setText(Name+"님의 생체데이터");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("유저").child("사용자").child(myUid).child("sensorInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mvibration = (String)dataSnapshot.child("vibration").getValue();
                String mtemp = (String) dataSnapshot.child("temp").getValue();
                String mheart = (String) dataSnapshot.child("pulse").getValue();
                heart_progress.setProgress(Integer.parseInt(mheart));
                heart_text.setText(mheart);
                temp_progress.setProgress(Integer.parseInt(mtemp));
                temp_text.setText(mtemp);
                fall_text.setText(mvibration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAKMIN", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAKMIN", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TAKMIN", "onDetach");

    }
}

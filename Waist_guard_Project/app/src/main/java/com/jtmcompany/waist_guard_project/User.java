package com.jtmcompany.waist_guard_project;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

//(파이어베이스인증한)사용자정보를 나타내는클래스
public class User {
    public String PhoneNumber;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    public User(String PhoneNumber){
        this.PhoneNumber=PhoneNumber;
    }

    //데이터목록에 추가할때마다 다른키를 갖게하기위해 고유키생성하여 고유키+휴대폰번호를 해쉬맵에 추가
    public Map<String, Object> toMap() {
        Map<String,Object> users = new HashMap<>();
        String key=mDatabase.child("users").push().getKey();
        users.put(key, PhoneNumber);

        return users;
    }
}

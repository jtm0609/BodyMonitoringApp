package com.jtmcompany.waist_guard_project;


import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

//(파이어베이스인증한)사용자정보를 나타내는클래스
public class User {
    private String PhoneNumber;
    private String Name;
    private String fcmToken= FirebaseInstanceId.getInstance().getToken();
    //DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();


    public User(String Name, String PhoneNumber){
        this.Name=Name;
        this.PhoneNumber=PhoneNumber;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    //데이터목록에 추가할때마다 다른키를 갖게하기위해 고유키생성하여 고유키+휴대폰번호를 해쉬맵에 추가
    public Map<String, Object> toMap() {
        Map<String,Object> users = new HashMap<>();
        //String key=mDatabase.child("유저").push().getKey();
        users.put("폰",PhoneNumber);
        users.put("토큰",fcmToken);

        return users;
    }
}

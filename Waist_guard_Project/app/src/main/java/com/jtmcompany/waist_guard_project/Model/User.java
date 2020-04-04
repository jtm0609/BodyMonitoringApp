package com.jtmcompany.waist_guard_project.Model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

//(파이어베이스인증한)사용자정보를 나타내는클래스
public class User {
    private String PhoneNumber=null;
    private String Name=null;
    private String fcmToken= FirebaseInstanceId.getInstance().getToken();
    private String userUid= FirebaseAuth.getInstance().getUid();
    //DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

    public String getFcmToken() {
        return fcmToken;
    }

    public String getUserUid() {
        return userUid;
    }



    public User(){ }

    public User(String Name){
        this.Name=Name;
    }

    public User(String Name, String PhoneNumber){
        this.Name=Name;
        this.PhoneNumber=PhoneNumber;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    //updateChild()를할때 유저클래스의 uid를 이용함
    public Map<String, Object> toMap() {
        Map<String,Object> users = new HashMap<>();
        //String Key=mDatabase.child("유저").push().getKey();
        users.put(userUid,userUid);

        return users;
    }
}

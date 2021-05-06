package com.jtmcompany.waist_guard_project.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jtmcompany.waist_guard_project.R;

import java.util.ArrayList;

//로그인화면 엑티비티
public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedpref;
    private FirebaseAuth mAuth;
    private ImageView safe_iv;
    private String[] permission={
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
    };
    private int REQUEST_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        requestPermission(permission); //위험권한부여


    }
    public void init(){
        mAuth=FirebaseAuth.getInstance();
        sharedpref=getSharedPreferences("user", Activity.MODE_PRIVATE);
        Button button=findViewById(R.id.start_bt);
        button.setOnClickListener(this);
        safe_iv=findViewById(R.id.safe_img);
        Glide.with(this).load(R.raw.safegif).into(safe_iv);
    }


    /** 자동 로그인 **/
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
            //로그인이 되어있고, 사용자라면
        if (currentUser != null && !sharedpref.getBoolean("isGuardian",false)) {
            Log.d("tak","사용자" );
            Intent intent = new Intent(getApplicationContext(), ConnectBlueToothActivity.class);
            startActivity(intent);
            finish();

            //로그인이 되어있고, 보호자라면
        } else if(currentUser!=null && sharedpref.getBoolean("isGuardian",false)){
            Log.d("tak","보호자" );
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }




    /** 퍼미션 체크(모두 허용됬는지) **/
    public boolean checkPermission(String[] permission){
       for(int i=0; i<permission.length; i++){
           String curPermission=permission[i];
           if(ContextCompat.checkSelfPermission(this,curPermission)!=PackageManager.PERMISSION_GRANTED)
               return false;
       }
       return true;
    }




    /** 퍼미션 요청**/
    //각 퍼미션의 거절을 2번이상 누르면 자동으로 묻지않음 처리가된다.
    public void requestPermission(String[] permission) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permission.length; i++) {
            String curPermission = permission[i];
            int permissionCheck = ContextCompat.checkSelfPermission(this, curPermission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                //과거에 거절을 누른경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, curPermission)) {
                    permissionList.add(curPermission);

                }
                //거절을 누르지 않은경우(최초)
                else{
                    permissionList.add(curPermission);
                }
            }
        }

        if(!permissionList.isEmpty()) {
            String[] permissionArray=new String[permissionList.size()];
            ActivityCompat.requestPermissions(this, permissionList.toArray(permissionArray), REQUEST_CODE);
        }
    }


    //시작 버튼
    @Override
    public void onClick(View view) {
        if(!checkPermission(permission)){
            Toast.makeText(this,"권한을 모두 허용하여야 정상적으로 시작이 가능합니다! ",Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent=new Intent(this, PhoneAuthActivity.class);
        startActivity(intent);
        finish();
    }
}

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedpref;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView safe_iv=findViewById(R.id.safe_img);
        Glide.with(this).load(R.raw.safegif).into(safe_iv);


        //위험권한부여
        String[] permission={
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
        };
        checkPermission(permission);

        mAuth=FirebaseAuth.getInstance();
        sharedpref=getSharedPreferences("user", Activity.MODE_PRIVATE);
        Button button=findViewById(R.id.start_bt);
        button.setOnClickListener(this);





    }

    //파이어베이스의 유저가 인증을했다면 메인엑티비티로자동으로넘어가고 아니라면 현재액티비티에머뭄(자동로그인)
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //mAuth.signOut();
        //사용자라면
        if (currentUser != null && !sharedpref.getBoolean("isGuardian",false)) {
            Log.d("tak","사용자" );
            Intent intent = new Intent(getApplicationContext(), UserServiceActivity.class);
            startActivity(intent);
            finish();
            //보호자라면
        } else if(currentUser!=null && sharedpref.getBoolean("isGuardian",false)){
            Log.d("tak","보호자" );
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }


    public void checkPermission(String[] permission){
        ArrayList<String> targetList=new ArrayList<String>();

        for(int i=0; i<permission.length;i++){
            String curPermission=permission[i];
            int permissionCheck= ContextCompat.checkSelfPermission(this,curPermission);
            if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한있음", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,curPermission)){
                Toast.makeText(this, curPermission+ "권한 설명필요함", Toast.LENGTH_SHORT).show();
            } else{
                targetList.add(curPermission);
            }
        }
        String[] targerts=new String[targetList.size()];
        targetList.toArray(targerts);
        ActivityCompat.requestPermissions(this,targerts,101);
    }

    //시작 버튼
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}

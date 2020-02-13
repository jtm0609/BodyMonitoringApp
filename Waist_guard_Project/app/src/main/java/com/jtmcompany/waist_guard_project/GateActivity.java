package com.jtmcompany.waist_guard_project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GateActivity extends AppCompatActivity {
    private static final String TAG = "TAKMIN";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private FirebaseAuth mAuth;
    private String mVerifyId;
    private boolean mVerification=false;

    //파이어베이스 전화번호입력후 sms를받아 인증하는방식
    //호출순서
//verifyPhoneNumber()->onCodesent()->onVerificationCompleted()->signinWithPhoneAuthCredential()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);

        final Button request_Msg_Button=findViewById(R.id.button); //인증번호요청 버튼
        final Button verify_Button=findViewById(R.id.verify_button); //인증번호확인 버튼
        final EditText mPhone_number=findViewById(R.id.phone_number); //전화번호입력 에디트텍스트
        final EditText verify_code=findViewById(R.id.verify_code); //인증번호입력 에디트텍스트
        final RadioGroup RadioGroup=findViewById(R.id.radio_group);

        //초기(라디오버튼을 누르지않는상태) 에디트텍스트와 버튼을 비활성화시킴
        request_Msg_Button.setEnabled(false);
        verify_Button.setEnabled(false);
        mPhone_number.setEnabled(false);
        verify_code.setEnabled(false);

        //callback
        mcallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            //즉시인증(인증코드를 보내거나 입력하지않고 전화번호를즉시인증 or
            //자동으로 수신되는 SMS를 감지하여 사용자의 개입없이 인증을 수행
            //콜백에 PhoneAuthCredential 객체 전달
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInwithPhoneAuthCrdential(credential);
                Log.d(TAG,"onVerificationCompleted");
            }

            //잘못된 인증요청에대한 응답호출
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG,"실패");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Log.d(TAG,"Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Log.d(TAG,"Quota exceeded.");
                    // [END_EXCLUDE]
                }

                //메시지전송실패시 전화번호입력창과 인증번호요청버튼일 활성화되고
                //인증번호입력창이 비활성화되고, 인증번호확인버튼이 비활성화됨
                mPhone_number.setEnabled(true);
                request_Msg_Button.setEnabled(true);
                verify_Button.setEnabled(false);
                verify_code.setEnabled(false);
            }

            //이 메소드는 제공된 전화번호로 인증 코드가 SMS를 통해 전송된 후에 호출
            //사용자가 인증 코드를 입력하면 인증 코드와 이 메서드에 전달된 인증 ID를 사용하여 PhoneAuthCredential 객체를 만들고 이 객체로 사용자를 로그인 처리
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: "+verificationId);
                mVerifyId=verificationId;

                //메시지가보내지면 인증번호입력창이 활성화됨
                verify_Button.setEnabled(true);
                verify_code.setEnabled(true);
            }
        };

        //인증번호요청버튼
        request_Msg_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoneVerification(mPhone_number.getText().toString());

            }
        });
        //
        mAuth=FirebaseAuth.getInstance();

        //라디오그룹선택
        //라디오버튼을누르면 전화번호입력창과 인증번호요청버튼이 활성화됨
        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int i) {
                if(group==RadioGroup){
                    if(i==R.id.user_RadioBt){
                        mPhone_number.setEnabled(true);
                        request_Msg_Button.setEnabled(true);

                    }
                    else if(i==R.id.guadian_RadioBt){
                        mPhone_number.setEnabled(true);
                        request_Msg_Button.setEnabled(true);
                    }
                }

            }
        });
        //인증번호확인버튼
        verify_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //입력한 인증번호와 인증ID를이용하여 PhoneAuthCredential객체를만들기위해 다음메소드호출->기존에있던 PhoneAuthCredential객체와 비교
                verifyPhoneNumberWithCode(mVerifyId,verify_code.getText().toString());
            }
        });

    }

    //파이어베이스의 유저가 인증을했다면 메인엑티비티로자동으로넘어가고 아니라면 현재액티비티에머뭄
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
        }
    }

    private void signInwithPhoneAuthCrdential(PhoneAuthCredential credential) {
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //로그인이 성공하면
                    if(task.isSuccessful()){
                        Toast.makeText(GateActivity.this,"로그인성공",Toast.LENGTH_SHORT).show();
                        Log.d("TAKMIN","signinWithPhoneAuthCredential");
                        //엑티비티전환
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    //로그인 실패
                    else{
                        Toast.makeText(GateActivity.this, "인증번호가틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

// 매개변수로받은 사용자의 전화번호를 확인하도록 요청->oncodesent()
    private void startPhoneVerification(String phoneNumber) {
        String modiPhone="";
        if(phoneNumber.startsWith("0")){
            modiPhone=phoneNumber.replaceFirst("0","+82");
        }
        else{
            Toast.makeText(this, "잘못된 번호입니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                modiPhone,
                60,
                TimeUnit.SECONDS,
                this,
                mcallbacks);

    }

    //입력한 인증번호와 메시지인증번호를 확인하는코드
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInwithPhoneAuthCrdential(credential);
    }
}

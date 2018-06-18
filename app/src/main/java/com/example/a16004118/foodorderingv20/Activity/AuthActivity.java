package com.example.a16004118.foodorderingv20.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a16004118.foodorderingv20.HttpRequest;
import com.example.a16004118.foodorderingv20.Object.User;
import com.example.a16004118.foodorderingv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etVerificationCode;
    private ProgressBar pbVerificationCode, pbPhoneNumber;
    private Button btnSendCode;
    private TextView tvError, tvInformation;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String TAG = "AuthActivity";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

    private int btnType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etVerificationCode = findViewById(R.id.etVerificationCode);

        pbVerificationCode = findViewById(R.id.pbVerificationCode);
        pbPhoneNumber = findViewById(R.id.pbPhoneNumber);

        tvError = findViewById(R.id.tvError);
        tvInformation = findViewById(R.id.tvInformation);

        btnSendCode = findViewById(R.id.btnSendCode);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnType == 0){

                    pbPhoneNumber.setVisibility(View.VISIBLE);
                    etPhoneNumber.setEnabled(false);
                    btnSendCode.setEnabled(false);

                    String phoneNumber = etPhoneNumber.getText().toString().trim();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks

                }else{
                    btnSendCode.setEnabled(false);
                    pbVerificationCode.setVisibility(View.VISIBLE);

                    String verificationCode = etVerificationCode.getText().toString().trim();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(AuthActivity.this, e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onVerificationFailed:" +  e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                btnType = 1;

                pbPhoneNumber.setVisibility(View.INVISIBLE);
                btnSendCode.setText("Verify Code");
                btnSendCode.setEnabled(true);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            String uid = user.getUid();

                            String url = "https://16004118.000webhostapp.com/getUserByUid.php?fbId=" + uid;

                            HttpRequest request = new HttpRequest(url);

                            request.setOnHttpResponseListener(mHttpResponseListenerGetUserByUid);
                            request.setMethod("GET");
//                            request.addData("fbId",uid);

                            request.execute();
                            // Code for step 1 end

//                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
//                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthActivity.this, task.getException()+"" , Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                Toast.makeText(AuthActivity.this, "Please enter valid verification code", Toast.LENGTH_LONG).show();
                                btnSendCode.setEnabled(true);
                                btnSendCode.setText("Send Verification Code");
                                btnType = 0;
                            }
                        }
                    }
                });
    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerGetUserByUid =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("getUserByUid Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.has("success")){
                            if (!jsonObj.getBoolean("success")){

                                String uid = mAuth.getCurrentUser().getUid();

                                String url = "https://16004118.000webhostapp.com/createUser.php?fbId=" + uid;

                                HttpRequest request = new HttpRequest(url);

                                request.setOnHttpResponseListener(mHttpResponseListenerCreateUser);
                                request.setMethod("POST");

                                request.execute();
                            }
                        } else{
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListenerCreateUser =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getBoolean("success")){
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("JSON Results", "onResponse: " + e.toString());
                    }
                }
            };
}

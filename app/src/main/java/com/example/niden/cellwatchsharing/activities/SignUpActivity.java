package com.example.niden.cellwatchsharing.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niden.cellwatchsharing.R;
import com.example.niden.cellwatchsharing.database.User;
import com.example.niden.cellwatchsharing.database.firebase;
import com.example.niden.cellwatchsharing.utils.DialogsUtils;
import com.example.niden.cellwatchsharing.utils.IntentUtils;
import com.example.niden.cellwatchsharing.utils.KeyboardUtils;
import com.example.niden.cellwatchsharing.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by niden on 16-Nov-17.
 */

public class SignUpActivity extends AppCompatActivity{

    private EditText inputEmail, inputPassword;
    Button btnSignIn, btnSignUp, btnResetPassword;
    ProgressDialog myDialog;
    public static FirebaseAuth auth;
    firebase mFirebase = new firebase();
    User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mUser.getFirebaseAuth();
        mUser.checkUserLogin(SignUpActivity.this);

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftKeyboard(SignUpActivity.this);
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    ToastUtils.displayMessageToast(SignUpActivity.this,"You must enter your Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.displayMessageToast(SignUpActivity.this,"You must enter your password");
                    return;
                }
                if (password.length() < 6) {
                    ToastUtils.displayMessageToast(SignUpActivity.this,"Password is too short, Enter at least 6 digits");
                    return;
                }else{
                    myDialog= DialogsUtils.showProgressDialog(SignUpActivity.this,"Signing up...");
                }
                mUser.createNewUser(SignUpActivity.this,email,password,myDialog);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


}
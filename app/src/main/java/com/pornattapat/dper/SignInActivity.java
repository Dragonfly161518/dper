package com.pornattapat.dper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    final String NON_EMAIL_OR_PWD = "อีเมลหรือรหัสผ่านว่าง กรุณาใส่ให้ครบด้วย";
    final String WRONG_EMAIL_OR_PWD = "wrong password or email";

    EditText inputEmail,inputPassword;
    Button btnSignUp,btnSignIn;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Auth State Check
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // User is signed out

                }
            }
        };
    }

    public void onClickSignIn(View view) {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(),NON_EMAIL_OR_PWD,Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),WRONG_EMAIL_OR_PWD,Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    public void onClickSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }
}

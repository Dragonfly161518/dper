package com.pornattapat.dper.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pornattapat.dper.R;
import com.pornattapat.dper.SignInActivity;

public class EndExam extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    Bundle b;

    TextView carrotRecieve,score,sum;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_exam);


        b = getIntent().getExtras();
        carrotRecieve = findViewById(R.id.carrot_recieve);
        sum = findViewById(R.id.sum);
        score = findViewById(R.id.score);

        if(b != null) {
            carrotRecieve.setText("+ "+ b.getInt("score")+"");
            sum.setText(b.getInt("correct") + "/" + b.getInt("total"));
            score.setText(b.getInt("score")+"");
        }

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }
            }
        };
    }

    public void back(View view) {
        finish();
    }

    public void nextExam(View view) {
        finish();

    }
}

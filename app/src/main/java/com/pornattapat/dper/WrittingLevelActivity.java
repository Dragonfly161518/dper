package com.pornattapat.dper;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pornattapat.dper.Exam.Exam;
import com.pornattapat.dper.Exam.StartExam;

public class WrittingLevelActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    ImageView level1,level2,lock1,lock2;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_writting_level);

        db = FirebaseFirestore.getInstance();
        level1 = findViewById(R.id.level_1);
        level2 = findViewById(R.id.level_2);
        lock1 = findViewById(R.id.lock_1);
        lock2 = findViewById(R.id.lock_2);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    db.collection("users").document(user.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User.level = document.getString("level");
                                            User.user = document.getString("user");
                                            User.email = document.getString("email");
                                            User.name = document.getString("name");
                                            User.sur_name = document.getString("sur_name");
                                        }

                                    }
                                }
                            });
                    holdAnimtion();
                } else {
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Integer.parseInt(User.level) >= 1) {
            lock1.setVisibility(View.INVISIBLE);
        }
        if(Integer.parseInt(User.level) >= 2) {
            lock2.setVisibility(View.INVISIBLE);
        }
    }

    private void holdAnimtion() {
        ImageView rabbit = findViewById(R.id.rabbit_animation);
        rabbit.setBackgroundResource(R.drawable.rabbit_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) rabbit.getBackground();
        frameAnimation.start();


        ImageView turtle = findViewById(R.id.turtle_animation);
        turtle.setBackgroundResource(R.drawable.turtle_animation);
        AnimationDrawable tleAnimation = (AnimationDrawable) turtle.getBackground();
        tleAnimation.start();
    }

    public void displayTest1(View view) {
        Toast.makeText(getApplicationContext(),"test1",Toast.LENGTH_SHORT).show();
    }

    public void exam(View view) {
        Exam.category = "Exam";
        if(lock1.getVisibility() != View.VISIBLE) {
            startActivity(new Intent(getApplicationContext(), StartExam.class));
        }
    }

    public void examVoice(View view) {
        Exam.category = "VoiceExam";
        if(lock2.getVisibility() != View.VISIBLE) {
            startActivity(new Intent(getApplicationContext(), StartExam.class));
        }
    }

    public void back(View view) {
        finish();
    }
}

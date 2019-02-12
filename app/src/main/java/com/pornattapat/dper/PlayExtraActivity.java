package com.pornattapat.dper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pornattapat.dper.Exam.EndExam;
import com.pornattapat.dper.Exam.Exam;
import com.pornattapat.dper.R;
import com.pornattapat.dper.SignInActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayExtraActivity extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    CountDownTimer mCountDown;

    TextView one,two,three,four,question;
    Button counter;

    String answer;

    int index=0,score=0,totalQuiz,correctAnswer;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_play_extra);

        db = FirebaseFirestore.getInstance();

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        counter = findViewById(R.id.counter);
        question = findViewById(R.id.questionExtra);
        Sprite effect = new FoldingCube();
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
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

    private void setQuiz(int index) {
        final int indexQuiz = index;
        db.collection(Exam.category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    totalQuiz = task.getResult().size() - 1;
                    Exam.totalQuiz = totalQuiz;
                    db.collection(Exam.category).document(indexQuiz + "").get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        final DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            one.setText(document.getString("A"));
                                            two.setText(document.getString("B"));
                                            three.setText(document.getString("C"));
                                            four.setText(document.getString("D"));
                                            answer = document.getString("correctAnswer");
                                            question.setText(document.getString("question"));
                                            Exam.TIMEOUT = document.getLong("time").intValue() * 1000;
                                            start(true);
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาดระหว่างการรับข้อมูล", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuiz = Exam.totalQuiz;
        setQuiz(index);
    }

    public void start(boolean countdown) {
        if(countdown) {
            mCountDown = new CountDownTimer(Exam.TIMEOUT, 1000) {
                @Override
                public void onTick(long l) {
                    counter.setText(String.valueOf(l / 1000));
                }

                @Override
                public void onFinish() {
                    mCountDown.cancel();
                    if (index + 1 >= totalQuiz) {
                        endGame();
                        return;
                    }
                    setQuiz(++index);
                }
            };
            mCountDown.start();
        }
    }

    public void solve() {
        int one = 0x00FF00;
        int two = 0xFF0000;
    }

    @Override
    public void onClick(View view) {
        if(mCountDown != null) {
            mCountDown.cancel();
        }
        if(index < totalQuiz) {
            String checkAnswer = "";
            switch(view.getId()) {
                case R.id.one:
                    checkAnswer = one.getText().toString();
                    break;
                case R.id.two:
                    checkAnswer = two.getText().toString();
                    break;
                case R.id.three:
                    checkAnswer = three.getText().toString();
                    break;
                case R.id.four:
                    checkAnswer = four.getText().toString();
                    break;

            }
            if(checkAnswer.equals(answer)) {
                score += 10;
                correctAnswer++;
            }

            if(index + 1 >= totalQuiz) {
                endGame();
                return;
            }
            setQuiz(++index);
        } else {
            endGame();
        }
    }

    public void endGame() {
        Intent intent = new Intent(getApplicationContext(), EndExam.class);
        intent.putExtra("score",score);
        intent.putExtra("total",totalQuiz);
        intent.putExtra("correct",correctAnswer);
        startActivity(intent);
        finish();
    }
}

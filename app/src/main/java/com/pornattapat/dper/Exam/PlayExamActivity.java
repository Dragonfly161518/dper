package com.pornattapat.dper.Exam;

import android.content.Intent;
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
import com.pornattapat.dper.R;
import com.pornattapat.dper.SignInActivity;
import com.squareup.picasso.Picasso;

public class PlayExamActivity extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    CountDownTimer mCountDown;

    ProgressBar progressBar;
    TextView leftAnswer;
    TextView rightAnswer;
    ImageView quizPicture;
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
        setContentView(R.layout.activity_play_exam);

        db = FirebaseFirestore.getInstance();


        leftAnswer = findViewById(R.id.leftAnswer);
        rightAnswer = findViewById(R.id.rightAnswer);
        counter = findViewById(R.id.counter);
        quizPicture = findViewById(R.id.quizPicture);
        progressBar = findViewById(R.id.progress);
        Sprite effect = new FoldingCube();
        progressBar.setIndeterminateDrawable(effect);
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

        progressBar.setVisibility(View.VISIBLE);
        leftAnswer.setVisibility(View.INVISIBLE);
        rightAnswer.setVisibility(View.INVISIBLE);
        counter.setVisibility(View.INVISIBLE);
        quizPicture.setVisibility(View.INVISIBLE);

        leftAnswer.setOnClickListener(this);
        rightAnswer.setOnClickListener(this);
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

                                           if(document.getBoolean("isVoiceExam")) {

                                           } else {
                                               Picasso.get().load(document.getString("picture")).into(quizPicture, new com.squareup.picasso.Callback() {

                                                   @Override
                                                   public void onSuccess() {
                                                       progressBar.setVisibility(View.INVISIBLE);
                                                       leftAnswer.setVisibility(View.VISIBLE);
                                                       rightAnswer.setVisibility(View.VISIBLE);
                                                       counter.setVisibility(View.VISIBLE);
                                                       quizPicture.setVisibility(View.VISIBLE);
                                                       leftAnswer.setText(document.getString("A"));
                                                       rightAnswer.setText(document.getString("B"));
                                                       answer = document.getString("correctAnswer");
                                                       Exam.TIMEOUT = document.getLong("time").intValue() * 1000;
                                                       mCountDown.start();
                                                   }

                                                   @Override
                                                   public void onError(Exception e) {

                                                   }
                                               });
                                           }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "พบปัญหาในการเข้าถึงข้อมูล กรุณาติดต่อทีมพัฒนา " + indexQuiz + " " + Exam.category + " " + Exam.totalQuiz, Toast.LENGTH_SHORT).show();
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

        mCountDown = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                counter.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                if(index + 1 >= totalQuiz) {
                    endGame();
                    return;
                }
                setQuiz(++index);
            }
        };
        Toast.makeText(getApplicationContext(),index+"",Toast.LENGTH_SHORT).show();
        setQuiz(index);
    }

    public void startExam() {
        startActivity(new Intent(getApplicationContext(),PlayExamActivity.class));
    }

    @Override
    public void onClick(View view) {
        mCountDown.cancel();
        if(index < totalQuiz) {
            String checkAnswer = "";
            switch(view.getId()) {
                case R.id.leftAnswer:
                    checkAnswer = leftAnswer.getText().toString();
                    break;
                case R.id.rightAnswer:
                    checkAnswer = rightAnswer.getText().toString();
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
        Toast.makeText(getApplicationContext(),
                "Score " + score + " | correct " + correctAnswer,Toast.LENGTH_SHORT).show();
    }
}

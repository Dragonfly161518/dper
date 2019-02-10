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
import android.widget.TextView;
import android.widget.Toast;

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
        leftAnswer = findViewById(R.id.leftAnswer);
        rightAnswer = findViewById(R.id.rightAnswer);
        counter = findViewById(R.id.counter);
        quizPicture = findViewById(R.id.quizPicture);

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
                counter.setText((int)l/1000 + "");
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                counter.setText("0");
                setQuiz(++index);
                Toast.makeText(getApplicationContext(),"finish",Toast.LENGTH_SHORT).show();
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

//            Button clickedBtn = (Button) view;
//            if(clickedBtn.getText().equals(answer)) {
//                score += 10;
//                correctAnswer++;
//                setQuiz(++index);
//            } else {
//
//            }

        } else {

        }
    }
}

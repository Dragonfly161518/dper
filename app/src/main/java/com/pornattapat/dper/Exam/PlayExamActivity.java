package com.pornattapat.dper.Exam;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.google.firebase.storage.StorageReference;
import com.pornattapat.dper.CustomImageView;
import com.pornattapat.dper.R;
import com.pornattapat.dper.SignInActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayExamActivity extends AppCompatActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    CountDownTimer mCountDown;

    ProgressBar progressBar;
    TextView leftAnswer;
    TextView rightAnswer,questionTag;
    ImageView quizPicture,speaker;
    Button counter;
    CardView choice1,choice2,questionMic;
    CustomImageView mic;
    Button stop;
    private StorageReference mStorage;

    String answer,urlMedia;

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

        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        mic = findViewById(R.id.mic);
        stop = findViewById(R.id.stop);
        stop.setVisibility(View.INVISIBLE);
        questionTag = findViewById(R.id.questionTag);
        mic.setVisibility(View.INVISIBLE);
        questionMic = findViewById(R.id.questionMic);
        questionMic.setVisibility(View.INVISIBLE);
        leftAnswer = findViewById(R.id.leftAnswer);
        rightAnswer = findViewById(R.id.rightAnswer);
        speaker = findViewById(R.id.speaker);
        speaker.setVisibility(View.INVISIBLE);
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
        speaker.setVisibility(View.INVISIBLE);

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
                                            if(document.getBoolean("isMic") == null) {
                                                if (document.getBoolean("isVoiceExam")) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    speaker.setVisibility(View.VISIBLE);
                                                    quizPicture.setVisibility(View.INVISIBLE);
                                                    counter.setVisibility(View.INVISIBLE);
                                                    leftAnswer.setVisibility(View.VISIBLE);
                                                    rightAnswer.setVisibility(View.VISIBLE);
                                                    answer = document.getString("correctAnswer");
                                                    leftAnswer.setText(document.getString("A"));
                                                    rightAnswer.setText(document.getString("B"));
                                                    urlMedia = document.getString("media");
                                                    start(false);
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
                                                            start(true);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                }
                                           } else {
                                                mic.setVisibility(View.VISIBLE);
                                                choice1.setVisibility(View.INVISIBLE);
                                                choice2.setVisibility(View.INVISIBLE);

                                                questionMic.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                questionTag.setText(document.getString("questionTag"));
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
                    solve();
                }
            };
            mCountDown.start();
        }
    }

    public void playMedia(View v) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(urlMedia);
        mp.prepare();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            }
        });
        mp.start();
    }

    public void solve() {
        if(Exam.category == "Exam" || Exam.category == "VoiceExam") {
            String one = "#00FF00";
            final String old = "#ffe450";
            String two = "#FF0000";
            if (leftAnswer.getText().equals(answer)) {
                choice1.setCardBackgroundColor(Color.parseColor(one));
                choice2.setCardBackgroundColor(Color.parseColor(two));
            } else {
                choice1.setCardBackgroundColor(Color.parseColor(two));
                choice2.setCardBackgroundColor(Color.parseColor(one));
            }
            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    choice1.setCardBackgroundColor(Color.parseColor(old));
                    choice2.setCardBackgroundColor(Color.parseColor(old));
                    setQuiz(++index);
                }
            }.start();
        } else {
            setQuiz(++index);
        }
    }

    @Override
    public void onClick(View view) {
        if(mCountDown != null) {
            mCountDown.cancel();
        }
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
            solve();
        } else {
            endGame();
        }
    }

    private MediaRecorder myAudioRecorder;
    private String outputFile;

    public void microphone(View v) {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp3";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
        stop.setVisibility(View.VISIBLE);
        mic.setVisibility(View.INVISIBLE);
    }

    public void stopmic(View v) {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        stop.setEnabled(false);

        Toast.makeText(getApplicationContext(), "เรียบร้อยแล้วครับ", Toast.LENGTH_LONG).show();
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

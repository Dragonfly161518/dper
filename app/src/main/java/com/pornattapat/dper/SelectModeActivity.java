package com.pornattapat.dper;

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
import com.pornattapat.dper.Exam.Exam;
import com.pornattapat.dper.Exam.StartExam;

public class SelectModeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    TextView carrotAmount;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_select_mode);

        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    setCarrotAmount();
                } else {
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                }
            }
        };
    }

    private void setCarrotAmount() {
        carrotAmount = findViewById(R.id.carrot_amount);
        String UID = user.getUid();
        db.collection("users").document(UID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String amount = document.getString("carrot");
                        if (amount == null) {
                            amount = "0";
                        }
                        carrotAmount.setText(amount);
                    } else {
                        Toast.makeText(getApplicationContext(), "พบปัญหาในการเข้าถึงข้อมูล กรุณาติดต่อทีมพัฒนา", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onClickSignOut(View view) {
        mAuth.signOut();
    }

    public void writtingLevel(View view) {
        Intent intent = new Intent(getApplicationContext(),WrittingLevelActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
//        Exam.category = "Extra";
//        Intent intent = new Intent(getApplicationContext(),StartExam.class);
//        startActivity(intent);

    }
}

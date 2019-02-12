package com.pornattapat.dper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BoardContentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_board_content);

        db = FirebaseFirestore.getInstance();

        Bundle b = getIntent().getExtras();
        final ProgressBar pgb = findViewById(R.id.progressBoardContent);
        final ImageView image = findViewById(R.id.pictureBoardContent);
        image.setVisibility(View.INVISIBLE);
        pgb.setVisibility(View.VISIBLE);
        if(b != null) {
            Picasso.get().load(b.getString("url")).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    image.setVisibility(View.VISIBLE);
                    pgb.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }


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

}

package com.pornattapat.dper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;

public class LibraryActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FirebaseUser user;

    RecyclerView recyclerViewPost;
    RecyclerView recyclerViewBoard;
    FirestoreRecyclerAdapter<Post, PostViewHolder> adapterPost;
    BoardAdapter adapterBoard;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_library);

        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    recyclerPost();
                    recyclerBoard();


                } else {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        };

    }

    public void recyclerPost() {
        Query query = FirebaseFirestore.getInstance()
                .collection("posts");
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class).build();
        adapterPost = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                holder.blog_user.setText(model.getUser());
                SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm");
                holder.blog_date.setText(format.format(model.getDate()) +"");
                holder.blog_desc.setText(model.getText());
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_user,viewGroup,false);
                return new PostViewHolder(view);
            }
        };
        recyclerViewPost = findViewById(R.id.postUser);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterPost.startListening();
        recyclerViewPost.setAdapter(adapterPost);
    }

    public void recyclerBoard() {
        Query query = FirebaseFirestore.getInstance()
                .collection("boards");
        FirestoreRecyclerOptions<Board> options = new FirestoreRecyclerOptions.Builder<Board>()
                .setQuery(query, Board.class).build();
        adapterBoard = new BoardAdapter(options);
        recyclerViewBoard = findViewById(R.id.boardRecycler);
        recyclerViewBoard.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterBoard.startListening();
        recyclerViewBoard.setAdapter(adapterBoard);

        adapterBoard.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String url = documentSnapshot.getString("pictureContent");
                Intent intent = new Intent(getApplicationContext(),BoardContentActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
    }

    public void directActivity(View view) {
        startActivity(new Intent(getApplicationContext(), DirectMessageActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapterPost != null) {
            adapterPost.startListening();
        }
        if(adapterBoard != null) {
            adapterBoard.startListening();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapterPost != null) {
            adapterPost.stopListening();
        }
        if(adapterBoard != null) {
            adapterBoard.stopListening();
        }
    }

}

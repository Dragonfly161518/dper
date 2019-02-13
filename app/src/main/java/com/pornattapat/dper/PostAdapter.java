package com.pornattapat.dper;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

import java.text.SimpleDateFormat;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

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


    class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView blog_user,blog_desc,blog_date;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            blog_user = itemView.findViewById(R.id.blog_user_name);
            blog_date = itemView.findViewById(R.id.blog_date);
            blog_desc  = itemView.findViewById(R.id.blog_desc);
        }

    }

}

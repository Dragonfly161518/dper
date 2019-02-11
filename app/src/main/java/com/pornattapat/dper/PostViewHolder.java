package com.pornattapat.dper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView blog_user,blog_desc,blog_date;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        blog_user = (TextView) itemView.findViewById(R.id.blog_user_name);
        blog_date = (TextView) itemView.findViewById(R.id.blog_date);
        blog_desc  = (TextView) itemView.findViewById(R.id.blog_desc);
    }

}

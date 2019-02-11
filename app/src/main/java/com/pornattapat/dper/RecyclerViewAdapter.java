package com.pornattapat.dper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<Movie> postList;

    RecyclerViewAdapter(List<Movie> postList){
        this.postList = postList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.blogUserName.setText(postList.get(position).getTitle());
        holder.blogDate.setText(postList.get(position).getTitle());
        holder.blogDesc.setText(postList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView blogUserName,blogDate,blogDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            blogUserName = itemView.findViewById(R.id.blog_user_name);
            blogDate = itemView.findViewById(R.id.blog_date);
            blogDesc = itemView.findViewById(R.id.blog_desc);
        }
    }
}

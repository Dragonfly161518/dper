package com.pornattapat.dper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BoardViewHolder extends RecyclerView.ViewHolder {

    public TextView textBoard;
    public ProgressBar progressBar;
    public ImageView pictureBoard;

    public BoardViewHolder(@NonNull View itemView) {
        super(itemView);
        textBoard = itemView.findViewById(R.id.textBoard);
        pictureBoard = itemView.findViewById(R.id.pictureBoard);
        progressBar = itemView.findViewById(R.id.progressBoard);
        progressBar.setVisibility(View.VISIBLE);
        pictureBoard.setVisibility(View.INVISIBLE);
        textBoard.setVisibility(View.INVISIBLE);
    }
}

package com.pornattapat.dper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BoardAdapter extends FirestoreRecyclerAdapter<Board, BoardAdapter.BoardViewHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BoardAdapter(@NonNull FirestoreRecyclerOptions<Board> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final BoardViewHolder holder, int position, @NonNull final Board model) {
        Picasso.get().load(model.getPictureHead()).into(holder.pictureBoard, new Callback() {
            @Override
            public void onSuccess() {
                holder.textBoard.setText(model.getHead());
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.textBoard.setVisibility(View.VISIBLE);
                holder.pictureBoard.setVisibility(View.VISIBLE);
                holder.pictureBoard.setContentDescription(model.getPictureContent() + "");
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board, viewGroup, false);
        return new BoardViewHolder(view);
    }

    class BoardViewHolder extends RecyclerView.ViewHolder {

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}


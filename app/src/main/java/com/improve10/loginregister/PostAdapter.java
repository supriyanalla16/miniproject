package com.improve10.loginregister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post1> postList;
    private OnPostListener onPostListener;

    public PostAdapter(List<Post1> postList, OnPostListener onPostListener) {
        this.postList = postList;
        this.onPostListener = onPostListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, onPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post1 post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle, textViewContent, textViewUsername;
        ImageView imageViewDelete;
        OnPostListener onPostListener;

        public PostViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            this.onPostListener = onPostListener;

            imageViewDelete.setOnClickListener(this);
        }

        public void bind(Post1 post) {
            textViewTitle.setText(post.getTitle());
            textViewContent.setText(post.getContent());
            textViewUsername.setText(post.getUsername());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageViewDelete) {
                onPostListener.onDeleteClick(getAdapterPosition());
            }
        }
    }

    public interface OnPostListener {
        void onDeleteClick(int position);
    }
}
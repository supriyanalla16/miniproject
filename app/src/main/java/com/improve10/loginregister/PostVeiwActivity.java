package com.improve10.loginregister;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class PostVeiwActivity extends AppCompatActivity {

    private LinearLayout linearLayoutPosts;
    private DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_veiw);

        linearLayoutPosts = findViewById(R.id.linearLayoutPosts);
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        loadPosts();
    }

    private void loadPosts() {
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayoutPosts.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post1 post = postSnapshot.getValue(Post1.class);
                    if (post != null) {
                        addPostToLayout(post);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostVeiwActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToLayout(Post1 post) {
        View postView = LayoutInflater.from(this).inflate(R.layout.activity_post_item, null);

        TextView titleView = postView.findViewById(R.id.postTitle);
        TextView contentView = postView.findViewById(R.id.postContent);
        TextView likesView = postView.findViewById(R.id.postLikes);
        ImageButton likeButton = postView.findViewById(R.id.likeButton);
        Button commentButton = postView.findViewById(R.id.commentButton);
        EditText commentInput = postView.findViewById(R.id.commentInput);
        LinearLayout commentsLayout = postView.findViewById(R.id.commentsLayout);

        if (titleView != null) titleView.setText(post.getTitle());
        if (contentView != null) contentView.setText(post.getContent());
        if (likesView != null) likesView.setText(String.valueOf(post.getLikes()));

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.addLike();
                databasePosts.child(post.getId()).setValue(post);
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInput.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    String userId = "user"; // Replace with actual user ID
                    post.addComment(userId, comment);
                    databasePosts.child(post.getId()).setValue(post);
                    commentInput.setText("");
                } else {
                    Toast.makeText(PostVeiwActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load and display comments
        if (post.getComments() != null) {
            for (Map.Entry<String, String> commentEntry : post.getComments().entrySet()) {
                TextView commentView = new TextView(this);
                commentView.setText(commentEntry.getValue());
                commentsLayout.addView(commentView);
            }
        }

        linearLayoutPosts.addView(postView);
    }
}
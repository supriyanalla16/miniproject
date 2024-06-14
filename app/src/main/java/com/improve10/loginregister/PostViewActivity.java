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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PostViewActivity extends AppCompatActivity {

    private LinearLayout linearLayoutPosts;
    private DatabaseReference databasePosts;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_veiw);

        linearLayoutPosts = findViewById(R.id.linearLayoutPosts);
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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
                Toast.makeText(PostViewActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToLayout(Post1 post) {
        View postView = LayoutInflater.from(this).inflate(R.layout.activity_post_item, null);

        TextView usernameView = postView.findViewById(R.id.postUsername);
        TextView titleView = postView.findViewById(R.id.postTitle);
        TextView contentView = postView.findViewById(R.id.postContent);
        TextView likesView = postView.findViewById(R.id.postLikes);
        ImageButton likeButton = postView.findViewById(R.id.likeButton);
        Button commentButton = postView.findViewById(R.id.commentButton);
        EditText commentInput = postView.findViewById(R.id.commentInput);
        LinearLayout commentsLayout = postView.findViewById(R.id.commentsLayout);

        usernameView.setText(post.getUsername());
        titleView.setText(post.getTitle());
        contentView.setText(post.getContent());
        likesView.setText(String.valueOf(post.getLikes()));

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
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        DocumentReference userRef = firestore.collection("users").document(userId);
                        userRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String username = task.getResult().getString("username");
                                Map<String, String> currentComments = post.getComments();
                                if (currentComments == null) {
                                    currentComments = new HashMap<>();
                                }
                                currentComments.put(username + "_" + System.currentTimeMillis(), comment);
                                post.setComments(currentComments);
                                databasePosts.child(post.getId()).setValue(post);
                                commentInput.setText("");
                            } else {
                                Toast.makeText(PostViewActivity.this, "Failed to fetch username", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(PostViewActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load and display comments
        if (post.getComments() != null) {
            for (Map.Entry<String, String> commentEntry : post.getComments().entrySet()) {
                TextView commentView = new TextView(this);
                commentView.setText(commentEntry.getKey().split("_")[0] + ": " + commentEntry.getValue());
                commentsLayout.addView(commentView);
            }
        }

        linearLayoutPosts.addView(postView);
    }
}
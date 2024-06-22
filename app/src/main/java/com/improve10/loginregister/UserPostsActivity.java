package com.improve10.loginregister;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPostsActivity extends AppCompatActivity implements PostAdapter.OnPostListener {

    private RecyclerView recyclerViewUserPosts;
    private PostAdapter postAdapter;
    private List<Post1> postList;

    private DatabaseReference databasePosts;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        recyclerViewUserPosts = findViewById(R.id.recyclerViewUserPosts);
        recyclerViewUserPosts.setHasFixedSize(true);
        recyclerViewUserPosts.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        recyclerViewUserPosts.setAdapter(postAdapter);

        databasePosts = FirebaseDatabase.getInstance().getReference("posts");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            loadUserPosts();
        }
    }

    private void loadUserPosts() {
        databasePosts.orderByChild("username").equalTo(currentUser.getDisplayName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post1 post = snapshot.getValue(Post1.class);
                            if (post != null) {
                                postList.add(post);
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserPostsActivity.this, "Failed to load posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDeleteClick(int position) {
        Post1 post = postList.get(position);
        String postId = post.getPostId();

        databasePosts.child(postId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserPostsActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    postList.remove(position);
                    postAdapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> Toast.makeText(UserPostsActivity.this, "Failed to delete post: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
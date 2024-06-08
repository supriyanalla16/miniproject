package com.improve10.loginregister;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                        TextView postView = new TextView(PostVeiwActivity.this);
                        postView.setText(post.getTitle() + "\n" + post.getContent());
                        postView.setPadding(16, 16, 16, 16);
                        linearLayoutPosts.addView(postView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}

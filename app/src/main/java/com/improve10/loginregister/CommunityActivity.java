package com.improve10.loginregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private Button buttonSubmit, buttonViewPosts, buttonUser;
    private DatabaseReference databasePosts;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent1);
        buttonSubmit = findViewById(R.id.buttonsSubmit);
        buttonViewPosts = findViewById(R.id.buttonsViewPosts);
        buttonUser = findViewById(R.id.butttonsuserpost);

        databasePosts = FirebaseDatabase.getInstance().getReference("posts");
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        buttonViewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, PostViewActivity.class);
                startActivity(intent);
            }
        });

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, UserPostsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitPost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DocumentReference userRef = firestore.collection("users").document(userId);
                userRef.get().addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null) {
                        String username = userTask.getResult().getString("username");

                        String id = databasePosts.push().getKey();
                        Post1 post = new Post1(id, username, title, content);
                        databasePosts.child(id).setValue(post).addOnCompleteListener(postTask -> {
                            if (postTask.isSuccessful()) {
                                runOnUiThread(() -> {
                                    editTextTitle.setText("");
                                    editTextContent.setText("");
                                    analyzeSentimentAndHandleResult(userRef, content);
                                });
                            } else {
                                runOnUiThread(() -> Toast.makeText(CommunityActivity.this, "Failed to submit post", Toast.LENGTH_SHORT).show());
                            }
                        });
                    } else {
                        Toast.makeText(CommunityActivity.this, "Failed to fetch username", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void analyzeSentimentAndHandleResult(DocumentReference userRef, String content) {
        SentimentAnalyzer.analyzeSentiment(content, new SentimentAnalyzer.SentimentCallback() {
            @Override
            public void onSuccess(float score) {
                if (score < 0) {
                    handleNegativeSentiment(userRef);
                } else {
                    resetNegativeSentimentCounter(userRef);
                    showPostSubmittedDialog();
                }
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(CommunityActivity.this, "Failed to analyze sentiment", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleNegativeSentiment(DocumentReference userRef) {
        userRef.update("negativeSentimentCount", FieldValue.increment(1))
                .addOnCompleteListener(negSentTask -> {
                    if (negSentTask.isSuccessful()) {
                        userRef.get().addOnCompleteListener(countTask -> {
                            if (countTask.isSuccessful() && countTask.getResult() != null) {
                                Long count = countTask.getResult().getLong("negativeSentimentCount");
                                if (count != null && count >= 5) {
                                    showConsultancyRedirectDialog();
                                    userRef.update("negativeSentimentCount", 0);  // Reset counter
                                } else {
                                    showNegativeSentimentDialog();
                                }
                            }
                        });
                    }
                });
    }

    private void resetNegativeSentimentCounter(DocumentReference userRef) {
        userRef.update("negativeSentimentCount", 0);
    }

    private void showPostSubmittedDialog() {
        // Ensure this code runs on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(CommunityActivity.this)
                        .setTitle("Post Submitted")
                        .setMessage("Your post has been successfully submitted.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

    private void showNegativeSentimentDialog() {
        // Implementation for negative sentiment dialog
    }

    private void showConsultancyRedirectDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Consecutive Negative Posts Detected")
                .setMessage("You have posted several negative posts in a row. We recommend you check out these resources to feel better.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        redirectToConsultancy();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void redirectToConsultancy() {
        Intent intent = new Intent(CommunityActivity.this, ConsultancyActivity.class);
        startActivity(intent);
    }
}
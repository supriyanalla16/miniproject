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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private Button buttonSubmit, buttonViewPosts;
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
    }

    private void submitPost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DocumentReference userRef = firestore.collection("users").document(userId);
                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String username = task.getResult().getString("username");

                        SentimentAnalyzer.analyzeSentiment(content, new SentimentAnalyzer.SentimentCallback() {
                            @Override
                            public void onSuccess(float score) {
                                String id = databasePosts.push().getKey();
                                Post1 post = new Post1(id, title, content, username);
                                databasePosts.child(id).setValue(post).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        runOnUiThread(() -> {
                                            editTextTitle.setText("");
                                            editTextContent.setText("");
                                            if (score < 0) {
                                                showNegativeSentimentDialog();
                                            } else {
                                                showPostSubmittedDialog();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(() -> Toast.makeText(CommunityActivity.this, "Failed to submit post", Toast.LENGTH_SHORT).show());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Exception e) {
                                runOnUiThread(() -> Toast.makeText(CommunityActivity.this, "Failed to analyze sentiment", Toast.LENGTH_SHORT).show());
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

    private void showPostSubmittedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Post Submitted")
                .setMessage("Your post has been successfully submitted.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with other operations if needed
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showNegativeSentimentDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Negative Sentiment Detected")
                .setMessage("Your post has been flagged for negative sentiment. We recommend checking out these resources to feel better.")
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
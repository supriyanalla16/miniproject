package com.improve10.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private Button buttonSubmit, buttonViewPosts;
    private DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent1);
        buttonSubmit = findViewById(R.id.buttonsSubmit);
        buttonViewPosts = findViewById(R.id.buttonsViewPosts);

        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        buttonViewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, PostVeiwActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitPost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            String id = databasePosts.push().getKey();
            Post1 post = new Post1(id, title, content);
            databasePosts.child(id).setValue(post);

            editTextTitle.setText("");
            editTextContent.setText("");

            Toast.makeText(this, "Post submitted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
        }
    }
}
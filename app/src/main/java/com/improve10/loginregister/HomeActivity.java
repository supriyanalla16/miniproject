package com.improve10.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button  Commutiy;
    Button Consultency;
    Button Links;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Commutiy  = findViewById(R.id.btn_community_blog);
        Consultency = findViewById(R.id.btn_consultant);
        Links = findViewById(R.id.btn_links);


        Commutiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Consultency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsultancyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinksActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
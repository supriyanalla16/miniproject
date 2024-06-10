package com.improve10.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    Button Commutiy;
    Button Consultency;
    Button Links;
    Button Refresh;
    Button Yoga;
    TextView Quote;
    RelativeLayout MainLayout;

    String[] quotes = {
            "My life is just beginning",
            "The only limit to our realization of tomorrow is our doubts of today.",
            "Life is 10% what happens to us and 90% how we react to it.",
            "With the new day comes new strength and new thoughts.",
            "The future belongs to those who believe in the beauty of their dreams."
    };

    int[] backgroundImages = {
            R.drawable.back8,
            R.drawable.back2,
            R.drawable.back3,
            R.drawable.back3,
            R.drawable.back4
    };

    int currentQuoteIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Commutiy = findViewById(R.id.btn_community_blog);
        Consultency = findViewById(R.id.btn_consultant);
        Links = findViewById(R.id.btn_links);
        Refresh = findViewById(R.id.btn_refresh);
        Quote = findViewById(R.id.tv_quote);
        MainLayout = findViewById(R.id.main_layout);
        Yoga = findViewById(R.id.btn_yoga);

        Yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),YogaActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuoteIndex = (currentQuoteIndex + 1) % quotes.length;
                Quote.setText(quotes[currentQuoteIndex]);
                MainLayout.setBackgroundResource(backgroundImages[currentQuoteIndex]);
            }
        });
    }
}
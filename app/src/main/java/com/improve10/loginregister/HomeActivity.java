package com.improve10.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    Button Community;
    Button Consultancy;
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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Community = findViewById(R.id.btn_community_blog);
        Consultancy = findViewById(R.id.btn_consultant);
        Links = findViewById(R.id.btn_links);
        Refresh = findViewById(R.id.btn_refresh);
        Quote = findViewById(R.id.tv_quote);
        MainLayout = findViewById(R.id.main_layout);
        Yoga = findViewById(R.id.btn_yoga);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Create notification channel
        createNotificationChannel();

        Yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YogaActivity.class);
                startActivity(intent);
            }
        });

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                    } else {
                        sendNotification();
                    }
                } else {
                    sendNotification();
                }
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });

        Consultancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsultancyActivity.class);
                startActivity(intent);
            }
        });

        Links.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LinksActivity.class);
            startActivity(intent);
        });

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuoteIndex = (currentQuoteIndex + 1) % quotes.length;
                Quote.setText(quotes[currentQuoteIndex]);
                MainLayout.setBackgroundResource(backgroundImages[currentQuoteIndex]);
            }
        });

        fetchUserId();
    }

    private void fetchUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setTitle(username);
                                }
                            } else {
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setTitle("User ID not found");
                                }
                            }
                        } else {
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle("Error fetching User ID");
                            }
                        }
                    });
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("No user is signed in");
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel for My Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("My Title")
                .setContentText("Hello")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Check for notification permission
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(1, builder.build());
        } else {
            // Handle case when notifications are not enabled
            // Maybe show a toast or log this event
            Toast.makeText(this, "Notifications are not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendNotification();
            } else {
                // Handle case when permission is denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
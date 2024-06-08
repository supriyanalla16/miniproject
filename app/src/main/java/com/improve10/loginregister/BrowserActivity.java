package com.improve10.loginregister;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class BrowserActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            new MyHTTPD(PORT).start();
            Log.i(TAG, "Server started on port " + PORT);
        } catch (IOException e) {
            Log.e(TAG, "Could not start server", e);
        }
    }

    private static class MyHTTPD extends NanoHTTPD {
        public MyHTTPD(int port) {
            super(port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            if (Method.POST.equals(session.getMethod())) {
                try {
                    Map<String, String> files = new HashMap<>();
                    session.parseBody(files);
                    String json = files.get("postData");
                    Log.i(TAG, "Received JSON: " + json);

                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    String url = jsonObject.get("url").getAsString();
                    String title = jsonObject.get("title").getAsString();

                    // Process the received data and notify if necessary
                    if (containsNegativeKeywords(url, title)) {
                        notifyUser("Negative search detected: " + title);
                    }

                    return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"status\":\"received\"}");
                } catch (IOException | NanoHTTPD.ResponseException e) {
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Internal Server Error");
                }
            }
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
        }

        private boolean containsNegativeKeywords(String url, String title) {
            String[] negativeKeywords = {"depression", "anxiety", "suicide", "stress", "self-harm"};
            for (String keyword : negativeKeywords) {
                if (url.toLowerCase().contains(keyword) || title.toLowerCase().contains(keyword)) {
                    return true;
                }
            }
            return false;
        }

        private void notifyUser(String message) {
            // Add code to notify the user (e.g., using a Notification)
            Log.i(TAG, "Notification: " + message);
        }
    }
}
package com.improve10.loginregister;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SentimentAnalyzer {

    private static final String API_URL = "https://language.googleapis.com/v1/documents:analyzeSentiment?key=AIzaSyB4rXKT-X-OUocNpsqgzD9lQDA7om8dbjo";

    public static void analyzeSentiment(String text, final SentimentCallback callback) {
        OkHttpClient client = new OkHttpClient();

        JSONObject document = new JSONObject();
        try {
            document.put("type", "PLAIN_TEXT");
            document.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("document", document);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONObject sentiment = jsonResponse.getJSONObject("documentSentiment");
                        float score = (float) sentiment.getDouble("score");
                        callback.onSuccess(score);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new Exception("Unsuccessful response"));
                }
            }
        });
    }

    public interface SentimentCallback {
        void onSuccess(float score);
        void onFailure(Exception e);
    }
}
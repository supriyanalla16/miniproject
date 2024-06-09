package com.improve10.loginregister;

import java.util.HashMap;
import java.util.Map;

public class Post1 {
    private String id;
    private String title;
    private String content;
    private int likes;
    private Map<String, String> comments;

    public Post1() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        this.likes = 0;
        this.comments = new HashMap<>();
    }

    public Post1(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.comments = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void addLike() {
        this.likes++;
    }

    public void addComment(String userId, String comment) {
        this.comments.put(userId, comment);
    }
}
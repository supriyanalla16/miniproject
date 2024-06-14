package com.improve10.loginregister;

import java.util.HashMap;
import java.util.Map;

public class Post1 {
    private String id;
    private String title;
    private String content;
    private String username;
    private int likes;
    private Map<String, String> comments;

    public Post1() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        this.likes = 0;
        this.comments = new HashMap<>();
    }

    public Post1(String id, String title, String content, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
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

    public String getUsername() {
        return username;
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

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public void addComment(String username, String comment) {
        this.comments.put(username + "_" + System.currentTimeMillis(), comment);
    }
}
package com.improve10.loginregister;

import java.util.HashMap;
import java.util.Map;

public class Post1 {
    private String postId;
    private String id;

    private String username;
    private String title;
    private String content;
    private int likes;
    private boolean liked;
    private Map<String, String> comments;

    // Default constructor required for calls to DataSnapshot.getValue(Post1.class)
    public Post1() {
        // Initialize default values
        this.id = id;

        this.likes = 0;
        this.liked = false;
        this.comments = new HashMap<>();
    }

    public Post1(String postId, String username, String title, String content) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.likes = 0;  // Initialize likes to 0
        this.liked = false;  // Initialize liked status to false
        this.comments = new HashMap<>();
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public void addComment(String username, String comment) {
        this.comments.put(username + "_" + System.currentTimeMillis(), comment);
    }
    public String getId() {
        return id;
    }
}
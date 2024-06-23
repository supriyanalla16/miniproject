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
    private Map<String, Boolean> likedUsers; // Map to store which users liked this post
    private Map<String, String> comments;

    public Post1() {
        this.likes = 0;
        this.likedUsers = new HashMap<>();
        this.comments = new HashMap<>();
    }

    public Post1(String postId, String username, String title, String content) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.likedUsers = new HashMap<>();
        this.comments = new HashMap<>();
    }

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

    public Map<String, Boolean> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Map<String, Boolean> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }
}
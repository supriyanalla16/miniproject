package com.improve10.loginregister;

public class Post1{
    private String id;
    private String title;
    private String content;

    public Post1() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post1(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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
}
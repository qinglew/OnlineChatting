package com.example.onlinechatting.entity;

public class Message {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;

    private int imaged;
    private String username;
    private String content;
    private int type;

    public Message() {
    }

    public Message(int imaged, String username, String content, int type) {
        this.imaged = imaged;
        this.username = username;
        this.content = content;
        this.type = type;
    }

    public int getImaged() {
        return imaged;
    }

    public void setImaged(int imaged) {
        this.imaged = imaged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

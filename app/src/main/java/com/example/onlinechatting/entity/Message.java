package com.example.onlinechatting.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @description 消息
 */
public class Message extends LitePalSupport {
    public static final int TYPE_SENT = 0;
    public static final int TYPE_RECEIVED = 1;

    private String phone;
    private int imaged;
    private String username;
    private String content;
    private int type;
    private long time;

    public Message() {
    }

    public Message(String phone, int imaged, String username, String content, int type, long time) {
        this.phone = phone;
        this.imaged = imaged;
        this.username = username;
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "phone='" + phone + '\'' +
                ", imaged=" + imaged +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", time=" + time +
                '}';
    }
}

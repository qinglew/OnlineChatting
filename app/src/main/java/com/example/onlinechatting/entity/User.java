package com.example.onlinechatting.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @description 用户
 */
public class User implements Parcelable {
    private String phone;      // 手机号
    private String username;   // 用户名
    private String password;   // 密码
    private int image;         // 图片
    private String desc;       // 个人简介

    public User() {
    }

    public User(String phone, String username, String password, int image, String desc) {
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.image = image;
        this.desc = desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image=" + image +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeInt(this.image);
        dest.writeString(this.desc);
    }

    protected User(Parcel in) {
        this.phone = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.image = in.readInt();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

package com.example.onlinechatting;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 */
public class ActivityManager {
    private static AppCompatActivity compatActivity;

    private static ActivityManager instance = new ActivityManager();

    public static List<Activity> activities;

    private ActivityManager() {
        activities = new ArrayList<>();
    }

    public static ActivityManager getInstance() {
        if (instance == null)
            instance = new ActivityManager();
        return instance;
    }

    /**
     * 新增活动
     * @param activity
     */
    public static void  addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除活动
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 结束所有活动，退出应用
     */
    public static void finishAll() {
        for (Activity activity : activities)
            if (!activity.isFinishing())
                activity.finish();
        activities.clear();
    }

    /**
     * 回到登陆界面
     */
    public static void finishAllExceptLogin() {
        for (Activity activity : activities) {
            if (!activity.isFinishing() && activity.getClass() != LoginActivity.class) {
                activity.finish();
            }
        }
    }

    public static AppCompatActivity getCompatActivity() {
        return compatActivity;
    }

    public static void setCompatActivity(AppCompatActivity compatActivity) {
        ActivityManager.compatActivity = compatActivity;
    }
}

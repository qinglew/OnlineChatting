package com.example.onlinechatting;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
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

    public static void  addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities)
            if (!activity.isFinishing())
                activity.finish();
        activities.clear();
    }
}

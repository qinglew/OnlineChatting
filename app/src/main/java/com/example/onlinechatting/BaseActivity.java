package com.example.onlinechatting;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

//    private ForceOfflineReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityManager.setCompatActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("ForceOffline", "onResume");
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.onlinechatting.FORCE_OFFLINE");
//        receiver = new ForceOfflineReceiver();
//        registerReceiver(receiver, intentFilter);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (receiver != null)
//            unregisterReceiver(receiver);
//    }
}

package com.example.onlinechatting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

public class ForceOfflineReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManager.getCompatActivity());
        builder.setTitle("警告!");
        builder.setMessage("由于您修改了密码，请重新登陆后再使用!");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", ((dialog, which) -> {
            ActivityManager.finishAll();
            Intent intent1 = new Intent(ActivityManager.getCompatActivity(), LoginActivity.class);
            ActivityManager.getCompatActivity().startActivity(intent1);
        })).show();
    }
}

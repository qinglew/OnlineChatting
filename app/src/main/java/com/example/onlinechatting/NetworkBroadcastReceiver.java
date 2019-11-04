package com.example.onlinechatting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = false;
        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getNetworkId() != -1) {
                isConnected = true;
            }
        }
        if (isConnected) {
            Toast.makeText(context, "已连接上局域网", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "局域网连接失败，请检查网络连接!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

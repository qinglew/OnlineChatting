package com.example.onlinechatting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinechatting.util.ClientTCPConnector;

public class LoginActivity extends BaseActivity {
    private EditText phone_et;
    private EditText password_et;
    private CheckBox rememberPassword;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String phone;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone_et = findViewById(R.id.phone_edit_text);
        password_et = findViewById(R.id.password_edit_text);
        rememberPassword = findViewById(R.id.rememberPassword);

        pref = getPreferences(MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            phone = pref.getString("phone", "");
            password = pref.getString("password", "");
            phone_et.setText(phone);
            password_et.setText(password);
            rememberPassword.setChecked(true);
        }



        /*
         WIFI检查广播
         */
        Intent intent = new Intent();
        intent.setAction("cn.edu.ncu.cleo.WIFI_NETWORK");
        intent.setPackage("com.example.onlinechatting");
        sendBroadcast(intent);
    }

    public void login(View view) {
        Log.d("LoginActivity", "login invoked.");
        phone = phone_et.getText().toString();
        password = password_et.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientTCPConnector clientTCPConnector = ClientTCPConnector.getInstance();
                    clientTCPConnector.connect();
                    clientTCPConnector.sendData("LOGIN|" + phone + "|" + password);
                    String info = clientTCPConnector.receiveData();
                    if (info != null && info.contains("SUCCESS")) {
                        editor = pref.edit();
                        if (rememberPassword.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        String[] parts = info.split("\\|");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", parts[1]);
                        intent.putExtra("icon_index", Integer.parseInt(parts[2]));
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    } else if (info != null && info.contains("ERROR")) {
                        String[] parts = info.split("\\|");
                        clientTCPConnector.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, parts[1], Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "未知错误!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    phone = data.getStringExtra("phone");
                    password = data.getStringExtra("password");
                    phone_et.setText(phone);
                    password_et.setText(password);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

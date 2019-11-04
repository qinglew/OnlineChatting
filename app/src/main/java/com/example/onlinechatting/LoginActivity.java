package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText username_et;
    private EditText password_et;

    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_et = findViewById(R.id.username_edit_text);
        password_et = findViewById(R.id.password_edit_text);

        /*
         WIFI检查广播
         */
        Intent intent = new Intent();
        intent.setAction("cn.edu.ncu.cleo.WIFI_NETWORK");
        intent.setPackage("com.example.onlinechatting");
        sendBroadcast(intent);
    }

    public void register(View view) {
        // TODO: 向后台服务器发送TCP/IP请求用以注册

    }

    public void login(View view) {
        username = username_et.getText().toString();
        password = password_et.getText().toString();
        if ("".equals(username)) {
            Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(password)) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("Cleo".equals(username) && "123456".equals(password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }
}

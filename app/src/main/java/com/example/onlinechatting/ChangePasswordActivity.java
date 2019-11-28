package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinechatting.entity.User;
import com.example.onlinechatting.util.ClientTCPConnector;

public class ChangePasswordActivity extends BaseActivity {
    private User user;

    public static int status;
    public static String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        EditText originPassword = findViewById(R.id.origin_password);
        EditText newPassword = findViewById(R.id.new_password);
        Button button = findViewById(R.id.sure_modify_password);

        button.setOnClickListener(v -> {
            String origin = originPassword.getText().toString();
            if ("".trim().equals(origin)) {
                Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!user.getPassword().equals(origin)) {
                Toast.makeText(this, "原密码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            String now = newPassword.getText().toString();
            if (now.trim().equals("")) {
                Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                ClientTCPConnector client = ClientTCPConnector.getInstance();
                client.sendData("UPDATE|PASSWORD|" + user.getPhone() + "|" + user.getUsername() + "|"
                        + now + "|" + user.getImage() + "|" + user.getDesc());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("password", now);
                    setResult(RESULT_OK, intent1);
                    finish();
                }
                else {
                    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                }
            }).start();
        });
    }
}

package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinechatting.entity.User;
import com.example.onlinechatting.util.ClientTCPConnector;

public class ModifyUsernameActivity extends BaseActivity {

    private User user;
    public static int status;
    public static String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_username);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        EditText editText = findViewById(R.id.username_edit_text);
        Button button = findViewById(R.id.commit);

        editText.setText(user.getUsername());

        button.setOnClickListener(v -> {
            String username = editText.getText().toString();
            if (username.equals("")) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                ClientTCPConnector client = ClientTCPConnector.getInstance();
                client.sendData("UPDATE|USERNAME|" + user.getPhone() + "|" + username + "|"
                        + user.getPassword() + "|" + user.getImage() + "|" + user.getDesc());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    // 成功
                    Intent intent1 = new Intent();
                    intent1.putExtra("username", username);
                    setResult(RESULT_OK, intent1);
                    finish();
                } else {
                    // 失败
                    runOnUiThread(() -> {
                        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });
    }
}

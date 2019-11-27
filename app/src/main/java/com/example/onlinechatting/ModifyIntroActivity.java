package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinechatting.entity.User;
import com.example.onlinechatting.util.ClientTCPConnector;

public class ModifyIntroActivity extends BaseActivity {

    private User user;
    public static int status;
    public static String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_intro);

        EditText editText = findViewById(R.id.person_intro);
        Button button = findViewById(R.id.commit_intro);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        editText.setText(user.getDesc());

        button.setOnClickListener(v -> {
            String desc = editText.getText().toString();
            Log.d("test", "onCreate: " + desc);
            if ("".trim().equals(desc)) {
                Toast.makeText(this, "个人简介不能为空", Toast.LENGTH_SHORT).show();
            }
            new Thread(() -> {
                ClientTCPConnector client = ClientTCPConnector.getInstance();
                client.sendData("UPDATE|DESC|" + user.getPhone() + "|" + user.getUsername() + "|"
                        + user.getPassword() + "|" + user.getImage() + "|" + desc);
                Log.d("test", "onCreate: " + desc);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    // 成功
                    Intent intent1 = new Intent();
                    intent1.putExtra("status", status);
                    intent1.putExtra("desc", desc);
                    setResult(RESULT_OK, intent1);
                    finish();
                } else {
                    // 失败
                    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                }
            }).start();
        });
    }
}

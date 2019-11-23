package com.example.onlinechatting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinechatting.util.ClientTCPConnector;

import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CircleImageView circleImageView;

    private String phone;
    private String username;
    private String password;
    private int iconIndex = 0;
    public static int imageId;
    private TypedArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        circleImageView = findViewById(R.id.icon_in_register);
        phoneEditText = findViewById(R.id.reg_phone);
        usernameEditText = findViewById(R.id.reg_username);
        passwordEditText = findViewById(R.id.reg_password);
        images = getResources().obtainTypedArray(R.array.icon_images);

        imageId = images.getResourceId(iconIndex, R.mipmap.icon1);
//        circleImageView.setImageResource(imageId);
        Log.d(TAG, "" + (R.mipmap.icon1 == images.getResourceId(iconIndex, 0)));
        Log.d("RegisterActivity", images.getResourceId(iconIndex, 0) + "");
        circleImageView.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, IconSelectActivity.class);
            startActivity(intent2);
        });
    }

    public void registerUser(View view) {
        // 获取所有输入信息并验证
        phone = phoneEditText.getText().toString();
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
        AtomicBoolean passed = new AtomicBoolean(true);
        if (!phone.matches("^[1][3|5|7|8][0-9]{9}$")) {

            dialog.setTitle("提示");
            dialog.setMessage("手机号码格式错误!");
            dialog.setCancelable(false);
            dialog.setNegativeButton("确定", (d, w) -> {
                passed.set(false);
            });
            dialog.show();
            return;
        }
        if (!username.matches("^\\w[\\w\\d]{5,20}")) {
            dialog.setTitle("提示");
            dialog.setMessage("用户名必须为字母开头，整体由5-20位字母数字组合而成!");
            dialog.setCancelable(false);
            dialog.setNegativeButton("确定", (d, w) -> {
                passed.set(false);
            });
            dialog.show();
            return;
        }
        if (!password.matches("[0-9a-zA-Z]{8,18}")) {
            dialog.setTitle("提示");
            dialog.setMessage("密码必须为8-18位字母数字组合!");
            dialog.setCancelable(false);
            dialog.setNegativeButton("确定", (d, w) -> {
                passed.set(false);
            });
            dialog.show();
            return;
        }
        // 网络处理逻辑
        new Thread(() -> {
            ClientTCPConnector clientTCPConnector = ClientTCPConnector.getInstance();
            clientTCPConnector.connect();
            clientTCPConnector.sendData("REGISTER|" + phone + "|" + username + "|" + password + "|" + iconIndex);
            String result = clientTCPConnector.receiveData();
            String[] parts = result.split("\\|");
            if ("ERROR".equals(parts[0])) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, parts[1], Toast.LENGTH_SHORT).show();
                });
                clientTCPConnector.close();
            } else if ("SUCCESS".equals(parts[0])) {
                // 注册成功后的逻辑
                clientTCPConnector.close();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                setResult(RESULT_OK, intent);
                finish();
            }

        }).start();
    }

    @Override
    protected void onStart() {
        circleImageView.setImageResource(imageId);
        for (int i = 0; i < images.length(); i++) {
            if (imageId == images.getResourceId(i, 0)) {
                iconIndex = i;
                break;
            }
        }
        super.onStart();
    }
}

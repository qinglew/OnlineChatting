package com.example.onlinechatting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinechatting.entity.User;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends BaseActivity {
    private static final String TAG = "PersonalInfoActivity";

    private CircleImageView icon;
    private TextView usernameEditText;
    private TextView phoneTextView;
    private TextView infoDesc;
    private Button quit;
    private Button modifyPassword;

    private ImageView changeIcon;
    private ImageView changeUsername;
    private ImageView changeDesc;

    public static User user;
    public static String info = "";
    private TypedArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        icon = findViewById(R.id.info_icon);
        usernameEditText = findViewById(R.id.info_username);
        phoneTextView = findViewById(R.id.info_phone);
        quit = findViewById(R.id.info_quit);
        modifyPassword = findViewById(R.id.modify_password);
        changeIcon = findViewById(R.id.change_icon);
        changeUsername = findViewById(R.id.change_username);
        changeDesc = findViewById(R.id.change_desc);
        infoDesc = findViewById(R.id.info_desc);

        images = getResources().obtainTypedArray(R.array.icon_images);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        icon.setImageResource(images.getResourceId(user.getImage(), 0));
        usernameEditText.setText(user.getUsername());
        phoneTextView.setText(user.getPhone());
        infoDesc.setText(user.getDesc());

        // 退出
        quit.setOnClickListener(v -> {
            ActivityManager.finishAll();
        });

        // 修改密码
        modifyPassword.setOnClickListener(v -> {
            Intent intent3 = new Intent(this, ChangePasswordActivity.class);
            Bundle bundle3 = new Bundle();
            bundle3.putParcelable("user", user);
            intent3.putExtras(bundle3);
            startActivityForResult(intent3, 3);
        });

        changeIcon.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ModifyIconActivity.class);
            startActivity(intent1);
        });

        changeUsername.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ModifyUsernameActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putParcelable("user", user);
            intent1.putExtras(bundle1);
            startActivityForResult(intent1, 1);
        });

        changeDesc.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, ModifyIntroActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("user", user);
            intent2.putExtras(bundle2);
            startActivityForResult(intent2, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String newUsername = data.getStringExtra("username");
                    user.setUsername(newUsername);
                    usernameEditText.setText(user.getUsername());
                    Toast.makeText(this, "修改用户名成功!", Toast.LENGTH_SHORT).show();
                }
            case 2:
                if (resultCode == RESULT_OK) {
                    String newDesc = data.getStringExtra("desc");
                    user.setDesc(newDesc);
                    infoDesc.setText(user.getDesc());
                }
            case 3:
                if (resultCode == RESULT_OK) {
                    String newPassword = data.getStringExtra("password");
                    user.setPassword(newPassword);
                    Log.d(TAG, "before sending broadcast");
                    // TODO: 发送广播强制下线重新登陆
//                    Intent forceOffLine = new Intent("com.example.onlinechatting.FORCE_OFFLINE");
////                    forceOffLine.setAction("com.example.onlinechatting.FORCE_OFFLINE");
//                    forceOffLine.setComponent(new ComponentName("com.example.onlinechatting",
//                            "com.example.onlinechatting.BaseActivity.ForceOfflineReceiver"));
//                    sendBroadcast(forceOffLine);
//                    Log.d(TAG, "after sending broadcast");
                    Intent intent1 = new Intent();
                    intent1.setAction("cn.edu.ncu.cleo.FORCE_OFFLINE");
                    intent1.setPackage("com.example.onlinechatting");
                    sendBroadcast(intent1);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        if ("SUCCESS".equals(info)) {
            Log.d(TAG, "" + user.getImage());
            icon.setImageResource(images.getResourceId(user.getImage(), 0));
        }
        else if ("ERROR".equals(info))
            Toast.makeText(this, "更新头像失败!", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}

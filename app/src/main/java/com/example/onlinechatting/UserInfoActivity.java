package com.example.onlinechatting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlinechatting.entity.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        CircleImageView icon = findViewById(R.id.user_icon_content);
        TextView phone = findViewById(R.id.phone_content);
        TextView username = findViewById(R.id.username_content);
        TextView desc = findViewById(R.id.desc_content);
        ImageView call = findViewById(R.id.call_icon);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        icon.setImageResource(MainActivity.icons.getResourceId(user.getImage(), 0));
        phone.setText(user.getPhone());
        username.setText(user.getUsername());
        if (user.getDesc() == null) {
            desc.setText("");
        } else {
            desc.setText(user.getDesc());
        }

        call.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setAction(Intent.ACTION_DIAL);
            intent1.setData(Uri.parse("tel:" + user.getPhone()));
            startActivity(intent1);
        });
    }
}

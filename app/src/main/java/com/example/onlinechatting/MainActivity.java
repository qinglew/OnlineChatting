package com.example.onlinechatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinechatting.adapter.MessageAdapter;
import com.example.onlinechatting.entity.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private EditText messageEdit;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        TextView usernameTitle = findViewById(R.id.username_title);
        usernameTitle.setText(username);

        initMessageList();
        messageAdapter = new MessageAdapter(messageList);

        /*
         UI组件初始化
         */
        drawerLayout = findViewById(R.id.drawable_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        messageEdit = findViewById(R.id.messageEdit);
        CircleImageView icon = findViewById(R.id.icon);

        /*
         toolbar和actionbar设置
         */
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        /*
         侧划栏中菜单设置
         */
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, "The function is not completed.",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /*
         RecyclerView设置
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);
    }

    /**
     * 初始化聊天列表
     */
    private void initMessageList() {
        messageList = new ArrayList<>();
        Message msg = new Message(R.drawable.panda_bambus_l,"Client",
                "Hello!", Message.TYPE_RECEIVED);
        messageList.add(msg);
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * 菜单事件绑定
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                // TODO: Something here to happened
                Toast.makeText(MainActivity.this,
                        "You clicked the Backup.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                delete(findViewById(R.id.fab));
                break;
            case R.id.setting:
                // TODO: 进入设置
                Toast.makeText(MainActivity.this,
                        "You clicked the Setting.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.quit:
                exit(findViewById(R.id.fab));
                break;
            case R.id.quit_login:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击退出时，弹出SnackBar
     * @param view
     */
    private void exit(View view) {
        Snackbar.make(view, "确定要退出应用吗？",
                Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
    }

    /**
     * 清空聊天记录时，弹出Snackbar
     * @param view
     */
    private void delete(View view) {
        Snackbar.make(view, "确定要清空聊天记录吗？", Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageList.clear();
                        messageAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    /**
     * FloatingActionAar点击事件: 返回顶部
     * @param view
     */
    public void scrollToTop(View view) {
        recyclerView.scrollToPosition(0);
    }

    /**
     * 按钮事件：发送消息
     * @param view
     */
    public void sendMessage(View view) {
        String content = messageEdit.getText().toString();
        if (!"".equals(content)) {
            Message msg = new Message(R.drawable.nav_icon, username,
                    content, Message.TYPE_SENT);
            messageList.add(msg);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            messageEdit.setText("");
        }
    }
}

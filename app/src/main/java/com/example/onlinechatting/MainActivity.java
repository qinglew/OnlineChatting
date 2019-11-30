package com.example.onlinechatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinechatting.adapter.MessageAdapter;
import com.example.onlinechatting.entity.Message;
import com.example.onlinechatting.entity.User;
import com.example.onlinechatting.util.ClientTCPConnector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private ClientTCPConnector clientTCPConnector;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private EditText messageEdit;
    private CircleImageView icon;
    private TextView usernameTitle;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    public static TypedArray icons;

    public static User user;

    private CircleImageView iconImage;
    private TextView phoneNumberTV;
    private TextView usernameTV;
    private TextView descriptionTV;
    private CircleImageView personInfo;

    private ServerMonitor serverMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        icons = getResources().obtainTypedArray(R.array.icon_images);
        initMessageList();
        messageAdapter = new MessageAdapter(messageList);

        /*
         * 侧划栏标题部分UI初始化
         */
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View view = navigationView.getHeaderView(0);
        RelativeLayout navHeader = view.findViewById(R.id.nav_header);
        iconImage = navHeader.findViewById(R.id.icon_image);
        phoneNumberTV = navHeader.findViewById(R.id.phone_number);
        usernameTV = navHeader.findViewById(R.id.username);
        descriptionTV = navHeader.findViewById(R.id.description);
        personInfo = navHeader.findViewById(R.id.person_info);

        /*
         UI组件初始化
         */
        drawerLayout = findViewById(R.id.drawable_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view);
        messageEdit = findViewById(R.id.messageEdit);
        usernameTitle = findViewById(R.id.username_title);
        usernameTitle.setText(user.getUsername());
        icon = findViewById(R.id.icon);

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
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_friends:
                            new Thread(() -> {
                                clientTCPConnector.sendData("LIST");
                            }).start();
                    }
                    return true;
                }
        );
        phoneNumberTV.setText(user.getPhone());
        usernameTV.setText(user.getUsername());
        iconImage.setImageResource(icons.getResourceId(user.getImage(), 0));
        if (user.getDesc() == null || user.getDesc().equals("null")) {
            descriptionTV.setText("");
        } else {
            descriptionTV.setText(user.getDesc());
        }
        icon.setImageResource(icons.getResourceId(user.getImage(), 0));
        icon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        personInfo.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, PersonalInfoActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putParcelable("user", user);
            intent1.putExtras(bundle1);
            startActivityForResult(intent1, 1);
        });

        /*
         RecyclerView设置
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);

        clientTCPConnector = ClientTCPConnector.getInstance();

        /*
         监控消息转发
         */
        serverMonitor = new ServerMonitor();
        new Thread(serverMonitor).start();
    }

    /**
     * 初始化聊天列表，加载历史消息
     */
    private void initMessageList() {
        messageList = LitePal.findAll(Message.class);
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
                Intent intent1 = new Intent(this, MessageHistoryActivity.class);
                startActivity(intent1);
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clientTCPConnector.sendData("LOGOUT");
                    }
                }).start();
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
                .setAction("确定", v -> {
                    new Thread(() -> clientTCPConnector.sendData("LOGOUT")).start();
                    ActivityManager.finishAll();
                }).show();
    }

    /**
     * 清空聊天记录时，弹出Snackbar
     * @param view
     */
    private void delete(View view) {
        Snackbar.make(view, "确定要清空聊天记录吗？", Snackbar.LENGTH_LONG)
                .setAction("确定", v -> {
                    LitePal.deleteAll(Message.class);  // 删除数据库中所有的消息记录
                    messageList.clear();
                    messageAdapter.notifyDataSetChanged();
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
        new Thread(() -> {
            String content = messageEdit.getText().toString();
            clientTCPConnector.sendData("MESSAGE|" + user.getPhone() + "|" + user.getUsername() + "|" + content);
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serverMonitor.exit = true;
        clientTCPConnector.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    user = bundle.getParcelable("user");
                    iconImage.setImageResource(icons.getResourceId(user.getImage(), 0));
                    icon.setImageResource(icons.getResourceId(user.getImage(), 0));
                    usernameTV.setText(user.getUsername());
                    usernameTitle.setText(user.getUsername());
                    if (user.getDesc() == null || user.getDesc().equals("null")) {
                        descriptionTV.setText("");
                    } else {
                        descriptionTV.setText(user.getDesc());
                    }
                }
                break;
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class ServerMonitor implements Runnable {
        public volatile boolean exit = false;

        @Override
        public void run() {
            while (!exit) {
                String info = clientTCPConnector.receiveData();
                if (info == null) {
                    exit = true;
                    break;
                }
                String[] parts = info.split("\\|");
                if ("MESSAGE".equals(parts[0])) {
                    Message message = new Message();
                    if (user.getPhone().equals(parts[1])) {
                        message.setPhone(user.getPhone());
                        message.setUsername(user.getUsername());
                        message.setType(Message.TYPE_SENT);
                        message.setImaged(icons.getResourceId(user.getImage(), 0));
                    }
                    else {
                        message.setPhone(parts[1]);
                        message.setUsername(parts[2]);
                        message.setType(Message.TYPE_RECEIVED);
                        message.setImaged(icons.getResourceId(Integer.parseInt(parts[3]), 0));
                    }
                    message.setContent(parts[4]);
                    message.setTime(new Date().getTime());
                    messageList.add(message);
                    message.save();  // 保存消息进数据库
                    runOnUiThread(() -> {
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        messageEdit.setText("");
                    });
                    continue;
                }
                if ("LOGOUT".equals(parts[0])) {
                    exit = true;
                }
                if ("IMAGE_ERROR".equals(parts[0])) {
                    PersonalInfoActivity.info = "ERROR";
                    Log.d(TAG, parts[1]);
                    user.setImage(PersonalInfoActivity.user.getImage());
                    continue;
                }
                if ("IMAGE_SUCCESS".equals(parts[0])) {
                    PersonalInfoActivity.info = "SUCCESS";
                    PersonalInfoActivity.user.setImage(user.getImage());
                    Log.d(TAG, Arrays.toString(parts));
                    String phone = parts[1];
                    int image = Integer.parseInt(parts[2]);
                    updateMessage(phone, image);
                    continue;
                }
                if ("USERNAME_SUCCESS".equals(parts[0])) {
                    ModifyUsernameActivity.status = 1;
                    ModifyUsernameActivity.info = "SUCCESS";
                    String phone = parts[1];
                    String username = parts[2];
                    updateMessage(phone, username);
                    continue;
                }
                if ("USERNAME_ERROR".equals(parts[0])) {
                    ModifyUsernameActivity.status = 0;
                    ModifyUsernameActivity.info = parts[1];
                    continue;
                }
                if ("DESC_SUCCESS".equals(parts[0])) {
                    ModifyIntroActivity.status = 1;
                    continue;
                }
                if ("DESC_ERROR".equals(parts[0])) {
                    ModifyIntroActivity.status = 0;
                    ModifyIntroActivity.info = parts[1];
                    continue;
                }
                if ("PASSWORD_SUCCESS".equals(parts[0])) {
                    ChangePasswordActivity.status = 1;
                    continue;
                }
                if ("PASSWORD_ERROR".equals(parts[0])) {
                    ChangePasswordActivity.status = 0;
                    ChangePasswordActivity.info = parts[1];
                    continue;
                }
                if ("LIST".equals(parts[0])) {
                    // TODO: 获取用户列表， 开启用户列表活动
                    List<User> users = new ArrayList<>();
                    String[] userStrings = parts[1].split("!");
                    for (String userString : userStrings) {
                        String[] attributes = userString.split(",");
                        User user = new User();
                        user.setPhone(attributes[0]);
                        user.setUsername(attributes[1]);
                        user.setImage(Integer.parseInt(attributes[2]));
                        user.setDesc(attributes[3]);
                        users.add(user);
                    }
                    Log.d(TAG, Arrays.toString(users.toArray()));
                    Intent intent1 = new Intent(MainActivity.this, UserListActivity.class);
                    UserListActivity.userList = users;
                    startActivity(intent1);
                }
            }
        }
    }

    /**
     * 更新数据库和消息列表中的消息的图像
     */
    private void updateMessage(String phone, int image) {
        runOnUiThread(() -> {
            messageList.clear();
            messageAdapter.notifyDataSetChanged();
            Message message = new Message();
            message.setImaged(icons.getResourceId(image, 0));
            message.updateAll("phone=?", phone);
            messageList.addAll(LitePal.findAll(Message.class));
            Log.d(TAG, "execute update UI");
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    /**
     * 更新数据库和消息列表中的用户名
     */
    private void updateMessage(String phone, String username) {
        runOnUiThread(() -> {
            messageList.clear();
            messageAdapter.notifyDataSetChanged();
            Message message = new Message();
            message.setUsername(username);
            message.updateAll("phone=?", phone);
            messageList.addAll(LitePal.findAll(Message.class));
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);
        });
    }
}

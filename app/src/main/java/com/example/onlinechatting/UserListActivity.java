package com.example.onlinechatting;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.onlinechatting.adapter.UserAdapter;
import com.example.onlinechatting.entity.User;

import java.util.Arrays;
import java.util.List;

public class UserListActivity extends BaseActivity {
    private UserAdapter userAdapter;
    public static List<User> userList;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView = findViewById(R.id.user_recycler_view);

        Log.d("UserListActivity", Arrays.toString(userList.toArray()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
    }
}

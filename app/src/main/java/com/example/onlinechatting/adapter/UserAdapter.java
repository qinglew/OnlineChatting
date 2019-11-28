package com.example.onlinechatting.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatting.MainActivity;
import com.example.onlinechatting.R;
import com.example.onlinechatting.UserInfoActivity;
import com.example.onlinechatting.entity.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userIcon.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            User user = userList.get(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            Intent intent1 = new Intent(mContext, UserInfoActivity.class);
            intent1.putExtras(bundle);
            mContext.startActivity(intent1);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(mContext).load(MainActivity
                .icons.getResourceId(user.getImage(), 0)).into(holder.userIcon);
        holder.username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userIcon;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.user_icon);
            username = itemView.findViewById(R.id.user_name);
        }
    }
}

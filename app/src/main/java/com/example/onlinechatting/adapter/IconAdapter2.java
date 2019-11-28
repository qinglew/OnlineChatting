package com.example.onlinechatting.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatting.MainActivity;
import com.example.onlinechatting.PersonalInfoActivity;
import com.example.onlinechatting.R;
import com.example.onlinechatting.entity.User;
import com.example.onlinechatting.util.ClientTCPConnector;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IconAdapter2 extends RecyclerView.Adapter<IconAdapter2.ViewHolder> {
    private static final String TAG = "IconAdapter2";

    private Context mContext;
    private List<Integer> mIconList;

    public IconAdapter2(List<Integer> iconList) {
        this.mIconList = iconList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.icon_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.icon.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            MainActivity.user.setImage(position);
            new Thread(() -> {
                ClientTCPConnector client = ClientTCPConnector.getInstance();
                User user = PersonalInfoActivity.user;
                client.sendData("UPDATE|IMAGE|" + user.getPhone() + "|" + user.getUsername() + "|"
                        + user.getPassword() + "|" + position + "|" + user.getDesc());
                Log.d(TAG, "发送数据成功!");
                Intent intent = new Intent(mContext, PersonalInfoActivity.class);
                mContext.startActivity(intent);
            }).start();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer integer = mIconList.get(position);
        Glide.with(mContext).load(integer).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mIconList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            icon = itemView.findViewById(R.id.icon_for_select);
        }
    }
}
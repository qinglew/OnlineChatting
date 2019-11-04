package com.example.onlinechatting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinechatting.R;
import com.example.onlinechatting.entity.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.message_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 绑定点击事件
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getType() == Message.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMessage.setText(message.getContent());
            holder.leftNameTxt.setText(message.getUsername());
        } else if (message.getType() == Message.TYPE_SENT) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMessage.setText(message.getContent());
            holder.rightNameTxt.setText(message.getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
        TextView leftNameTxt;
        TextView rightNameTxt;
        CircleImageView leftImage;
        CircleImageView rightImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            leftLayout = itemView.findViewById(R.id.leftLayout);
            rightLayout = itemView.findViewById(R.id.rightLayout);
            leftMessage = itemView.findViewById(R.id.leftMessageTxt);
            rightMessage = itemView.findViewById(R.id.rightMessageTxt);
            leftNameTxt = itemView.findViewById(R.id.leftNameTxt);
            rightNameTxt = itemView.findViewById(R.id.rightNameTxt);
            leftImage = itemView.findViewById(R.id.leftImage);
            rightImage = itemView.findViewById(R.id.rightImage);
        }
    }
}

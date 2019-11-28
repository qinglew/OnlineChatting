package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.example.onlinechatting.adapter.MessageAdapter;
import com.example.onlinechatting.entity.Message;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_history);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.message_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Date date = new Date();
        calendarView.setDate(date.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long mills1 = calendar.getTimeInMillis();
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day + 1);
        long mills2 = calendar.getTimeInMillis();
        List<Message> messages = LitePal
                .where("time between ? and ?", "" + mills1, "" + mills2)
                .find(Message.class);
        messageList.clear();
        messageList.addAll(messages);
        messageAdapter.notifyDataSetChanged();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            long m1 = calendar.getTimeInMillis();
            int d = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, d + 1);
            long m2 = calendar.getTimeInMillis();
            List<Message> messages1 = LitePal
                    .where("time between ? and ?", "" + m1, "" + m2)
                    .find(Message.class);
            messageList.clear();
            messageList.addAll(messages1);
            messageAdapter.notifyDataSetChanged();
        });
    }
}

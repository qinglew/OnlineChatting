package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;

import com.example.onlinechatting.adapter.IconAdapter;
import com.example.onlinechatting.adapter.IconAdapter2;

import java.util.ArrayList;
import java.util.List;

public class ModifyIconActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_icon);

        RecyclerView icons = findViewById(R.id.icons_recycler_view);
        List<Integer> images = new ArrayList<>();
        TypedArray igs = getResources().obtainTypedArray(R.array.icon_images);
        for (int i = 0; i < igs.length(); i++) {
            images.add(igs.getResourceId(i, 0));
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        icons.setLayoutManager(layoutManager);
        IconAdapter2 iconAdapter = new IconAdapter2(images);
        icons.setAdapter(iconAdapter);
    }
}

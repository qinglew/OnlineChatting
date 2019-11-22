package com.example.onlinechatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;

import com.example.onlinechatting.adapter.IconAdapter;

import java.util.ArrayList;
import java.util.List;

public class IconSelectActivity extends BaseActivity {
    private List<Integer> images;
    private RecyclerView icons;
    private IconAdapter iconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);

        icons = findViewById(R.id.icons_recycler_view);
        images = new ArrayList<>();
        TypedArray igs = getResources().obtainTypedArray(R.array.icon_images);
        for (int i = 0; i < igs.length(); i++) {
            images.add(igs.getResourceId(i, 0));
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        icons.setLayoutManager(layoutManager);
        iconAdapter = new IconAdapter(images);
        icons.setAdapter(iconAdapter);
    }
}

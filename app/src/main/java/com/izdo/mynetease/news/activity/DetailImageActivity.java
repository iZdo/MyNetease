package com.izdo.mynetease.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.adapter.DetailImageAdapter;
import com.izdo.mynetease.news.bean.DetailWebImage;

import java.util.ArrayList;

/**
 * Created by iZdo on 2017/9/11.
 */

public class DetailImageActivity extends Activity {

    ViewPager viewpager;
    ArrayList<View> views;
    private ArrayList<DetailWebImage> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        views = new ArrayList<>();
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        mImages = (ArrayList<DetailWebImage>) getIntent().getSerializableExtra("image");

        if (mImages != null) {
            for (DetailWebImage tmp : mImages) {
                View view = View.inflate(this, R.layout.item_detail_img, null);
                views.add(view);
            }
        }

        DetailImageAdapter adapter = new DetailImageAdapter(mImages, this, views);
        viewpager.setAdapter(adapter);

    }
}

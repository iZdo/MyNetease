package com.izdo.mynetease.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.bean.Banner;
import com.izdo.mynetease.news.bean.HotDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by iZdo on 2017/9/9.
 */

public class BannerAdapter extends PagerAdapter {

    ArrayList<View> view;
    ArrayList<Banner> banners;
    DisplayImageOptions mOptions;
    int size;
    ArrayList<HotDetail> details;
    Context mContext;

    public BannerAdapter(ArrayList<View> view, ArrayList<Banner> banners, Context context) {
        this.view = view;
        this.banners = banners;
        mContext = context;
        size = view.size();

        // 建造者模式 -> 创建一个复杂的对象
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final int realPosition = position % size;

        View tmp = view.get(realPosition);
        ImageView image = (ImageView) tmp.findViewById(R.id.img);

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "广告条新闻暂无法显示", Toast.LENGTH_SHORT).show();
            }
        });

        Banner banner = banners.get(realPosition);
        ImageLoader.getInstance().displayImage(banner.getImgsrc(), image, mOptions);
        container.addView(tmp);

        return tmp;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setDetails(ArrayList<HotDetail> details) {
        this.details = details;
    }
}

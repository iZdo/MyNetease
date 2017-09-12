package com.izdo.mynetease.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.bean.DetailWebImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by iZdo on 2017/9/11.
 */

public class DetailImageAdapter extends PagerAdapter {

    ArrayList<DetailWebImage> mImages;
    Context mContext;
    ArrayList<View> mViews;
    DisplayImageOptions mOptions;

    public DetailImageAdapter(ArrayList<DetailWebImage> images, Context context, ArrayList<View> views) {
        mImages = images;
        mContext = context;
        mViews = views;

        mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo);
        ImageLoader.getInstance().displayImage(mImages.get(position).getSrc(), photoView, mOptions);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

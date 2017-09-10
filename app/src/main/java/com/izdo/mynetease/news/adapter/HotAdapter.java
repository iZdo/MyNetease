package com.izdo.mynetease.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.bean.HotDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iZdo on 2017/9/7.
 */

public class HotAdapter extends BaseAdapter {

    ArrayList<HotDetail> mHotDetails;
    LayoutInflater mInflater;
    DisplayImageOptions mOptions;

    public HotAdapter(ArrayList<HotDetail> hotDetails, Context context) {
        mHotDetails = hotDetails;
        mInflater = LayoutInflater.from(context);

        // 建造者模式 -> 创建一个复杂的对象
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

    }

    @Override
    public int getCount() {
        return mHotDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mHotDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        HotDetail detail = mHotDetails.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_hot, null);

            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.source = (TextView) convertView.findViewById(R.id.source);
            holder.reply_count = (TextView) convertView.findViewById(R.id.reply_count);
            holder.special = (TextView) convertView.findViewById(R.id.special);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        initViews(holder, detail);

        return convertView;
    }

    public void initViews(ViewHolder holder, HotDetail detail) {
        holder.title.setText(detail.getTitle());
        holder.source.setText(detail.getSource());
        holder.reply_count.setText(detail.getReplyCount() + "跟帖");

        ImageLoader.getInstance().displayImage(detail.getImg(), holder.icon, mOptions);
    }

    public void addDate(List<HotDetail> add) {
        if (mHotDetails == null) {
            mHotDetails = new ArrayList<>();
        }
        mHotDetails.addAll(add);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView source;
        TextView reply_count;
        TextView special;
    }
}

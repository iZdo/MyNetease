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
import com.izdo.mynetease.news.bean.SpecialItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by iZdo on 2017/9/17.
 */

public class SpecialAdapter extends BaseAdapter {

    int title = 0;
    int content = 1;

    ArrayList<SpecialItem> data;
    Context mContext;
    LayoutInflater mInflater;
    DisplayImageOptions mOptions;
    ImageLoader image;

    public SpecialAdapter(ImageLoader image ,ArrayList<SpecialItem> data, Context context) {
        this.data = data;
        mContext = context;
        this.image = image;
        mOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        SpecialItem item = data.get(i);
        if (type == title) {
            //返回一个标题类
            TitleViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_special_title, null);
                viewHolder = new TitleViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TitleViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(item.getIndex() + " " + item.getTitle_name());
        } else {
            ContentViewHolder viewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_special, null);
                viewHolder = new ContentViewHolder();
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.name = (TextView) convertView.findViewById(R.id.title);
                viewHolder.vote = (TextView) convertView.findViewById(R.id.reply_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ContentViewHolder) convertView.getTag();
            }

            initHolder(viewHolder, item);
        }
        return convertView;
    }

    public void initHolder(ContentViewHolder holder,SpecialItem backs ){

        holder.name.setText(backs.getLtitle());
        holder.vote.setText(String.valueOf(backs.getVotecount()));

        image.displayImage(backs.getImgsrc(),holder.icon,mOptions);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isTitle() ? title : content;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class TitleViewHolder {
        TextView title;
    }

    class ContentViewHolder {
        ImageView icon;
        TextView name;
        TextView vote;

    }
}

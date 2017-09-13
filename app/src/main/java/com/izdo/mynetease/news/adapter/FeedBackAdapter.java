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
import com.izdo.mynetease.news.bean.FeedBack;
import com.izdo.mynetease.news.bean.FeedBacks;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iZdo on 2017/9/12.
 */

public class FeedBackAdapter extends BaseAdapter {

    int type_title = 0;
    int type_content = 1;

    ArrayList<FeedBacks> date;
    LayoutInflater mInflater;

    DisplayImageOptions mOptions;

    public FeedBackAdapter(ArrayList<FeedBacks> date, Context context) {
        this.date = date;
        mInflater = LayoutInflater.from(context);

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.biz_tie_user_avater_default)
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)
                .showImageOnFail(R.drawable.biz_tie_user_avater_default)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int i) {
        return date.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);

        if (type == type_title) {
            TitleViewHolder viewHolder;
            if (view == null) {
                view = mInflater.inflate(R.layout.item_feed_title, null);
                viewHolder = new TitleViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                view.setTag(viewHolder);
            } else {
                viewHolder = (TitleViewHolder) view.getTag();
            }

        } else {
            ContentViewHolder viewHolder;
            FeedBacks feedBacks = date.get(i);

            if (view == null) {
                view = mInflater.inflate(R.layout.item_feedback, null);
                viewHolder = new ContentViewHolder();
                viewHolder.icon = (CircleImageView) view.findViewById(R.id.profile_image);
                viewHolder.name = (TextView) view.findViewById(R.id.net_name);
                viewHolder.from = (TextView) view.findViewById(R.id.net_from);
                viewHolder.content = (TextView) view.findViewById(R.id.content);
                viewHolder.vote = (TextView) view.findViewById(R.id.like);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ContentViewHolder) view.getTag();
            }

            initHolder(viewHolder, feedBacks);
        }

        return view;
    }

    public void initHolder(ContentViewHolder holder, FeedBacks backs) {
        FeedBack back =  backs.getLastData();
        holder.name.setText(back.getN());
        holder.from.setText(back.getF());
        holder.content.setText(back.getB());
        holder.vote.setText(back.getV());

        ImageLoader.getInstance().displayImage(back.getTimg(),holder.icon,mOptions);
    }

    // 返回页面数据的类型
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // 返回相应类型的view
    @Override
    public int getItemViewType(int position) {
        FeedBacks feedBacks = date.get(position);
        if (feedBacks.getIsTitle()) {
            return type_title;
        } else {
            return type_content;
        }
    }

    class TitleViewHolder {
        TextView title;
    }

    class ContentViewHolder {
        ImageView icon;
        TextView name;
        TextView from;
        TextView time;
        TextView content;
        TextView vote;
    }
}

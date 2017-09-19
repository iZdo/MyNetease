package com.izdo.mynetease.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.izdo.mynetease.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by iZdo on 2017/9/14.
 */

public class ShowAdapter extends BaseAdapter {
    ArrayList<String> titles;
    Context mContext;

    boolean isShowDel = false;

    public ShowAdapter(ArrayList<String> titles, Context context) {
        this.titles = titles;
        mContext = context;
    }

    public ShowAdapter(Context context) {
        this.titles = new ArrayList<>();
        mContext = context;
    }

    public ShowAdapter(String[] titles, Context context) {
        this.titles = new ArrayList<>();
        this.titles.addAll(Arrays.asList(titles));
        mContext = context;
    }

    public void setShowDel() {
        this.isShowDel = !isShowDel;
        notifyDataSetChanged();
    }

    public void setShowDelUnable() {
        this.isShowDel = false;
        notifyDataSetChanged();
    }

    public String delATitle(int index) {
        String title = titles.get(index);
        titles.remove(index);
        notifyDataSetChanged();
        return title;
    }

    public void addAData(String title) {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add(title);
        notifyDataSetChanged();
    }

    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < titles.size(); i++) {
            String tmp = titles.get(i);
            builder.append(tmp);
            if (i != titles.size() - 1) {
                builder.append("-");
            }
        }
        return builder.toString();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return titles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean isShowDel() {
        return isShowDel;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        String name = titles.get(i);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_show, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.del = (ImageView) convertView.findViewById(R.id.del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(name);

        if (isShowDel) {
            viewHolder.del.setVisibility(View.VISIBLE);

        } else {
            viewHolder.del.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView del;
    }
}

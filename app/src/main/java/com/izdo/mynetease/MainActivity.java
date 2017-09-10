package com.izdo.mynetease;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.izdo.mynetease.news.fragment.EmptyFragment;
import com.izdo.mynetease.news.fragment.NewsFragment;

public class MainActivity extends AppCompatActivity {

    long lastBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tab_Host);

        // 获取tab的标题
        String[] titles = getResources().getStringArray(R.array.tab_title);
        // 背景图
        int[] icons = new int[]{R.drawable.news_selector, R.drawable.reading_selector, R.drawable.video_selector, R.drawable.topic_selector, R.drawable.mine_selector};
        // fragment
        Class[] classes = new Class[]{NewsFragment.class, EmptyFragment.class, EmptyFragment.class, EmptyFragment.class, EmptyFragment.class};

        // 绑定
        tabHost.setup(this, getSupportFragmentManager(), R.id.content);

        for (int i = 0; i < titles.length; i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec("" + i);
            tab.setIndicator(getEveryView(this, titles, icons, i));
            tabHost.addTab(tab, classes[i], null);
        }

        // 设置默认选中的页面
        tabHost.setCurrentTabByTag("0");

    }

    /**
     * 为tab设置title和icon
     */
    public View getEveryView(Context context, String[] titles, int[] icons, int index) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View title_view = inflater.inflate(R.layout.item_title, null);
        TextView title = (TextView) title_view.findViewById(R.id.title);
        ImageView icon = (ImageView) title_view.findViewById(R.id.icon);
        // 设置内容
        title.setText(titles[index]);
        icon.setImageResource(icons[index]);

        return title_view;

    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastBackTime < 1000) {
            finish();
        } else {
            Toast.makeText(this, "再次点击退出新闻客户端", Toast.LENGTH_SHORT).show();
        }
        lastBackTime = nowTime;
    }

}

package com.izdo.mynetease;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.izdo.mynetease.news.bean.ShowTabEvent;
import com.izdo.mynetease.news.fragment.EmptyFragment;
import com.izdo.mynetease.news.fragment.NewsFragment;
import com.izdo.mynetease.util.FragmentTabHost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    long lastBackTime = 0;
    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById(R.id.tab_Host);

        int version = getSDKVersion();
        if (version >= 19) {
            ImageView image = (ImageView) findViewById(R.id.status);
            int height = getStatusHeight(this);
            image.getLayoutParams().height = height;
            image.setBackgroundColor(Color.RED);
        }

        // 注册eventBus
        EventBus.getDefault().register(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showOrHideTab(ShowTabEvent event) {
        boolean isShow = event.isShow();
        if (!isShow) {
            TabWidget tabWidget = tabHost.getTabWidget();
            tabWidget.setVisibility(View.GONE);
        } else {
            TabWidget tabWidget = tabHost.getTabWidget();
            tabWidget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取SDK版本
     */
    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
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

    /**
     * 获取状态栏的高度
     * 通过反射获取系统状态栏的高度
     */
    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}

package com.izdo.mynetease.news.news_inner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.activity.DetailActivity;
import com.izdo.mynetease.news.adapter.BannerAdapter;
import com.izdo.mynetease.news.adapter.HotAdapter;
import com.izdo.mynetease.news.bean.Banner;
import com.izdo.mynetease.news.bean.Hot;
import com.izdo.mynetease.news.bean.HotDetail;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.HttpResponse;
import com.izdo.mynetease.util.HttpUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iZdo on 2017/9/6.
 */

public class HotFragment extends Fragment implements ViewPager.OnPageChangeListener, AbsListView.OnScrollListener {

    ListView mListView;
    // 放置轮播图
    ArrayList<Banner> mBanners;
    // 放置新闻
    ArrayList<HotDetail> mHotDetails;
    ArrayList<View> views;
    ArrayList<ImageView> dot_imgs;

    MyHandler mHandler;
    HotAdapter adapter;
    LayoutInflater inflater;

    // 加载成功
    private final static int INIT_SUCCESS = 0;
    // 加载更多成功
    private final static int UPDATE_SUCCEDD = 1;

    // 轮播图相关控件
    ViewPager viewpager;
    BannerAdapter bAdapter;
    TextView bannerTitle;
    LinearLayout dots;

    int startIndex = 0;
    int endIndex = 0;
    // int pageSize = 20;
    // 取页面的次数
    int count = 0;

    boolean isToEnd = false;
    boolean isHttpRequestIng = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCollection();

        initView();

        getDate(true);
    }

    private void initCollection() {
        mBanners = new ArrayList<>();
        mHotDetails = new ArrayList<>();
        views = new ArrayList<>();
        dot_imgs = new ArrayList<>();
    }

    private void initView() {
        mHandler = new MyHandler(this);
        inflater = LayoutInflater.from(getActivity());
        View head = inflater.inflate(R.layout.include_banner, null);
        //将轮播图控件加入listView
        mListView.addHeaderView(head);
        // 添加监听
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailActivity.class);
                HotDetail detail = adapter.getDateByIndex(position - mListView.getHeaderViewsCount());
                intent.putExtra(DetailActivity.DOCID, detail.getDocid());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });

        viewpager = (ViewPager) head.findViewById(R.id.viewpager);
        viewpager.addOnPageChangeListener(this);
        bannerTitle = (TextView) head.findViewById(R.id.title);
        dots = (LinearLayout) head.findViewById(R.id.dots);
    }

    private void getDate(final boolean isInit) {
        if (isHttpRequestIng) {
            return;
        }
        isHttpRequestIng = true;
        HttpUtil util = HttpUtil.getInstance();
        calIndex();
        String url = Constant.getHotUrl(startIndex, endIndex);
        util.getDate(url, new HttpResponse<Hot>(Hot.class) {
            @Override
            public void onError(String msg) {
                isHttpRequestIng = false;
            }

            @Override
            public void onSucceed(Hot hot) {
                isHttpRequestIng = false;
                if (hot != null && hot.getT1348647909107() != null) {
                    count++;
                    List<HotDetail> details = hot.getT1348647909107();
                    // 取出第0位包含轮播图的数据
                    if (isInit) {
                        HotDetail tmp_banner = details.get(0);
                        List<Banner> banners = tmp_banner.getAds();
                        mBanners.addAll(banners);

                        // 除去轮播图数据
                        details.remove(0);
                        mHotDetails.addAll(details);

                        mHandler.sendEmptyMessage(INIT_SUCCESS);
                    } else {
                        Message message = mHandler.obtainMessage(UPDATE_SUCCEDD);
                        message.obj = details;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
    }

    public void calIndex() {
        if (count == 0) {
            endIndex = startIndex + 20;

        } else {
            startIndex = endIndex;
            endIndex = startIndex + 20;
        }
    }

    // 处理listView数据
    public void initDate() {
        adapter = new HotAdapter(mHotDetails, getActivity());
        mListView.setAdapter(adapter);
    }

    public void update(List<HotDetail> newDate) {
        if (adapter == null) {
            mHotDetails = new ArrayList<>();
            mHotDetails.addAll(newDate);
            adapter = new HotAdapter(mHotDetails, getActivity());
            mListView.setAdapter(adapter);
        } else {
            adapter.addDate(newDate);
        }
    }

    public void initBanner() {
        if (mBanners != null && mBanners.size() > 0) {
            for (int i = 0; i < mBanners.size(); i++) {
                View view = inflater.inflate(R.layout.item_banner, null);
                views.add(view);

                ImageView dot = new ImageView(getActivity());
                dot.setImageResource(R.drawable.gray_dot);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(0, 0, 10, 0);
                dots.addView(dot, p);
                dot_imgs.add(dot);
            }
            bAdapter = new BannerAdapter(views, mBanners);
            viewpager.setAdapter(bAdapter);
            int half = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mBanners.size();
            viewpager.setCurrentItem(half);

            //设置默认显示的数据
            setImageDot(0);
            setBannerTitle(0);
        }
    }

    public void setImageDot(int index) {
        int size = dot_imgs.size();
        int realPosition = index % size;
        for (int i = 0; i < size; i++) {
            ImageView dot = dot_imgs.get(i);
            if (i == realPosition) {
                dot.setImageResource(R.drawable.white_dot);
            } else {
                dot.setImageResource(R.drawable.gray_dot);
            }
        }
    }

    public void setBannerTitle(int index) {
        int size = dot_imgs.size();
        int realPosition = index % size;
        //显示默认数据
        bannerTitle.setText(mBanners.get(realPosition).getTitle());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageDot(position);
        setBannerTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 监听滑动的状态回调
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isToEnd) {
            //获取更多数据
            getDate(false);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (absListView.getLastVisiblePosition() == totalItemCount - 1) {
            isToEnd = true;
        } else {
            isToEnd = false;
        }
    }

    static class MyHandler extends Handler {
        WeakReference<HotFragment> weak_fragment;

        public MyHandler(HotFragment fragment) {
            this.weak_fragment = new WeakReference<HotFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HotFragment hot = weak_fragment.get();
            if (hot == null) {
                return;
            }
            switch (msg.what) {
                case INIT_SUCCESS:
                    hot.initDate();
                    hot.initBanner();
                    break;
                case UPDATE_SUCCEDD:
                    List<HotDetail> date = (List<HotDetail>) msg.obj;
                    hot.update(date);
                default:
                    break;
            }
        }
    }
}

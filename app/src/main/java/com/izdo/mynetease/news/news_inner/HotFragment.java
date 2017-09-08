package com.izdo.mynetease.news.news_inner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.izdo.mynetease.R;
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

public class HotFragment extends Fragment {

    ListView mListView;
    // 放置轮播图
    ArrayList<Banner> mBanners;
    // 放置新闻
    ArrayList<HotDetail> mHotDetails;
    MyHandler mHandler;
    HotAdapter adapter;
    LayoutInflater inflater;

    private final static int INIT_SUCCESS = 0;

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
        mBanners = new ArrayList<>();
        mHotDetails = new ArrayList<>();
        mHandler = new MyHandler(this);

        HttpUtil util = HttpUtil.getInstance();
        util.getDate(Constant.HOT_URL, new HttpResponse<Hot>(Hot.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSucceed(Hot hot) {
                if (hot != null && hot.getT1348647909107() != null) {
                    List<HotDetail> details = hot.getT1348647909107();
                    // 取出第0位包含轮播图的数据
                    HotDetail tmp_banner = details.get(0);
                    List<Banner> banners = tmp_banner.getAds();
                    mBanners.addAll(banners);

                    // 删除轮播图数据
                    details.remove(0);
                    mHotDetails.addAll(details);

                    mHandler.sendEmptyMessage(INIT_SUCCESS);

                }
            }
        });
    }

    // 处理listView数据
    public void initDate(){
        adapter = new HotAdapter(mHotDetails,getActivity());
        mListView.setAdapter(adapter);
    }

    static class MyHandler extends Handler {
        WeakReference<HotFragment> weak_fragment;

        public MyHandler(HotFragment fragment) {
            this.weak_fragment = new WeakReference<HotFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HotFragment hot = weak_fragment.get();

            switch (msg.what) {
                case INIT_SUCCESS:
                    hot.initDate();
                    break;
                default:
                    break;
            }
        }
    }
}

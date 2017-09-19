package com.izdo.mynetease.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.adapter.FeedBackAdapter;
import com.izdo.mynetease.news.bean.FeedBack;
import com.izdo.mynetease.news.bean.FeedBacks;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.HttpResponse;
import com.izdo.mynetease.util.HttpUtil;
import com.izdo.mynetease.util.JsonUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import static com.izdo.mynetease.R.id.listView;

/**
 * Created by iZdo on 2017/9/12.
 */

public class FeedBackActivity extends Activity {

    ListView mListView;
    ArrayList<FeedBacks> backs;
    FeedBackAdapter mAdapter;
    InnerHandler mInnerHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mInnerHandler = new InnerHandler(this);

        mListView = (ListView) findViewById(listView);

        String docid = getIntent().getStringExtra(DetailActivity.DOCID);
        String url = Constant.getFeedbackUrl(docid);

        backs = new ArrayList<>();
        HttpUtil util = HttpUtil.getInstance();
        util.getData(url, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSucceed(String string) {
                try {
                    // 获取所有数据
                    JSONObject js = new JSONObject(string);
                    // 取出hotPosts对应的JSONArray
                    JSONArray array = js.optJSONArray("hotPosts");
                    FeedBacks title = new FeedBacks();
                    title.setIsTitle(true);
                    title.setTitle("热门跟帖");
                    backs.add(title);

                    for (int i = 0; i < array.length(); i++) {
                        FeedBacks feedBacks = new FeedBacks();
                        feedBacks.setIsTitle(false);
                        JSONObject tmp = array.optJSONObject(i);
                        Iterator<String> keys = tmp.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject everyJson = tmp.optJSONObject(key);
                            FeedBack feedBack = JsonUtil.parseJson(everyJson.toString(), FeedBack.class);
                           // Logger.i(feedBack.getF());

                            feedBack = modifyFeedBack(feedBack);
                            // 记录每一个回帖的序号
                            feedBack.setIndex(Integer.valueOf(key));


                            feedBacks.add(feedBack);
                        }
                        // 根据key排序
                        feedBacks.sort();
                        backs.add(feedBacks);
                    }
                    mInnerHandler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FeedBack modifyFeedBack(FeedBack feedBack) {
        FeedBack newFeedBack = feedBack;
        String username = newFeedBack.getN();
        newFeedBack.setF(newFeedBack.getF().replace("网易", ""));
        newFeedBack.setF(newFeedBack.getF().replace("&nbsp;", " "));
        newFeedBack.setF(newFeedBack.getF().replace("：", " "));
        newFeedBack.setF(newFeedBack.getF().replace(username + "", ""));

        Logger.i(newFeedBack.getVip());

        if(newFeedBack.getN()==null)
            newFeedBack.setN("火星网友");

        return newFeedBack;
    }

    public void init() {
        mAdapter = new FeedBackAdapter(backs, this);
        mListView.setAdapter(mAdapter);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    static class InnerHandler extends Handler {
        WeakReference<FeedBackActivity> activity;

        public InnerHandler(FeedBackActivity activity) {
            this.activity = new WeakReference<FeedBackActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FeedBackActivity feedBackActivity = activity.get();
            if (feedBackActivity == null) {
                return;
            }
            feedBackActivity.init();
        }
    }
}

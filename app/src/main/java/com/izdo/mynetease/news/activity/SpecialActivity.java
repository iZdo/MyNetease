package com.izdo.mynetease.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.adapter.SpecialAdapter;
import com.izdo.mynetease.news.adapter.SpecialTitleAdapter;
import com.izdo.mynetease.news.bean.SpecialItem;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.HttpResponse;
import com.izdo.mynetease.util.HttpUtil;
import com.izdo.mynetease.util.JsonUtil;
import com.izdo.mynetease.util.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.izdo.mynetease.R.id.listView;

/**
 * Created by iZdo on 2017/9/16.
 */
public class SpecialActivity extends Activity {

    public static final String SPECIAL_ID = "special_id";
    String specialId;
    ArrayList<SpecialItem> mItems;

    String banner_src;
    String sname;

    Handler mHandler;
    SpecialAdapter mAdapter;
    ListView mListView;

    ImageView banner;
    NoScrollGridView grid;

    ArrayList<String> titles;
    ArrayList<Integer> indexs;
    SpecialTitleAdapter s_adapter;

    ImageLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);

        mImageLoader = ImageLoader.getInstance();
        titles = new ArrayList<>();
        specialId = getIntent().getStringExtra(SPECIAL_ID);

        mItems = new ArrayList<>();
        indexs = new ArrayList<>();
        mListView = (ListView) findViewById(listView);

        View head = View.inflate(this, R.layout.include_special_head, null);
        mListView.addHeaderView(head);
        mListView.setOnScrollListener(new PauseOnScrollListener(mImageLoader, false, true));

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                s_adapter = new SpecialTitleAdapter(titles, SpecialActivity.this);
                grid.setAdapter(s_adapter);
                ImageLoader.getInstance().displayImage(banner_src, banner);

                mAdapter = new SpecialAdapter(mImageLoader, mItems, SpecialActivity.this);
                mListView.setAdapter(mAdapter);
            }
        };

        banner = (ImageView) head.findViewById(R.id.banner);
        grid = (NoScrollGridView) head.findViewById(R.id.gird);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int head = mListView.getHeaderViewsCount();
                int index = indexs.get(position);
                mListView.setSelection(index + head);
            }
        });


        String url = Constant.getSpecialUrl(specialId);
        HttpUtil util = HttpUtil.getInstance();
        util.getData(url, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSucceed(String string) {

                try {
                    JSONObject jsonObject = new JSONObject(string);

                    JSONObject real_js = jsonObject.optJSONObject(specialId);

                    banner_src = real_js.optString("banner");
                    sname = real_js.optString("sname");

                    JSONArray topics = real_js.optJSONArray("topics");

                    for (int i = 0; i < topics.length(); i++) {
                        JSONObject tmp = topics.optJSONObject(i);

                        // 栏目标题数据
                        int index = tmp.optInt("index");
                        String name = tmp.optString("tname");

                        titles.add(name);

                        // 封装标题栏的数据
                        SpecialItem item_title = new SpecialItem();
                        item_title.setTitle(true);
                        item_title.setTitle_name(name);
                        item_title.setIndex(index + "/" + topics.length());

                        mItems.add(item_title);
                        indexs.add(mItems.size() - 1);

                        JSONArray docs = tmp.optJSONArray("docs");

                        for (int j = 0; j < docs.length(); j++) {
                            JSONObject item = docs.optJSONObject(j);
                            //每个栏目的对应的数据
                            SpecialItem item_bean = JsonUtil.parseJson(item.toString(), SpecialItem.class);
                            item_bean.setTitle(false);
                            mItems.add(item_bean);
                        }
                    }

                    mHandler.sendEmptyMessage(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

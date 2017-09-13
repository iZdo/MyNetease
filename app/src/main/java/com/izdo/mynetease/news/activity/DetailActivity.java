package com.izdo.mynetease.news.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.bean.DetailWeb;
import com.izdo.mynetease.news.bean.DetailWebImage;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.HttpResponse;
import com.izdo.mynetease.util.HttpUtil;
import com.izdo.mynetease.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by iZdo on 2017/9/11.
 */

public class DetailActivity extends SwipeBackActivity {

    public static final String DOCID = "doc";
    private String mDoc_id;

    // 新闻的html内容
    private String body;
    private MyHandler mHandler;

    public static final int INIT_SUCCESS = 0;

    private WebView mWebView;

    private int replayCount;

    private TextView replayCountTextView;
    private LinearLayout share_outer;
    private EditText feeback;
    private TextView send;
    private RelativeLayout parent;
    private boolean hasFocus = false;

    // 滑动退出
    private SwipeBackLayout mSwipeBackLayout;
    ArrayList<DetailWebImage> images;

    @JavascriptInterface
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWebView = (WebView) findViewById(R.id.webView);
        feeback = (EditText) findViewById(R.id.feeback);
        share_outer = (LinearLayout) findViewById(R.id.share_outer);
        send = (TextView) findViewById(R.id.send);
        parent = (RelativeLayout) findViewById(R.id.parent);

        final Drawable left = getResources().getDrawable(R.drawable.biz_pc_main_tie_icon);

        left.setBounds(0, 0, 30, 30);

        feeback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean fouces) {
                hasFocus = fouces;
                if (fouces) {
                    //有焦点
                    feeback.setCompoundDrawables(null, null, null, null);
                    feeback.setHint("");
                    share_outer.setVisibility(View.GONE);
                    send.setVisibility(View.VISIBLE);
                } else {
                    //无焦点
                    feeback.setCompoundDrawables(left, null, null, null);
                    feeback.setHint("写跟帖");
                    share_outer.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                }
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        replayCountTextView = (TextView) findViewById(R.id.replayCount);
        replayCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DetailActivity.this, FeedBackActivity.class);
                intent.putExtra(DOCID, mDoc_id);
                startActivity(intent);
            }
        });

        mWebView.addJavascriptInterface(this, "demo");

        mHandler = new MyHandler(this);
        mDoc_id = getIntent().getStringExtra(DOCID);

        HttpUtil util = HttpUtil.getInstance();
        String url = Constant.getDetailUrl(mDoc_id);
        util.getData(url, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSucceed(String json) {
                try {
                    JSONObject js = new JSONObject(json);
                    JSONObject need_js = js.optJSONObject(mDoc_id);
                    DetailWeb web = JsonUtil.parseJson(need_js.toString(), DetailWeb.class);

                    if (web != null) {
                        body = web.getBody();
                        images = (ArrayList<DetailWebImage>) web.getImg();
                        for (int i = 0; i < images.size(); i++) {
                            String src = images.get(i).getSrc();
                            String imageTag = "<img src='" + src + "'onclick=\"show()\"/>";
                            String tag = "<!--IMG#" + i + "-->";
                            body = body.replace(tag, imageTag);
                        }
                        String titleHTML = "<p><span style='font-size:18px;'><strong>" + web.getTitle() + "</strong></span></p>";
                        titleHTML = titleHTML + "<p><span style='color:#666666;'>" + web.getSource() + "&nbsp&nbsp" + web.getPtime() + "</span></p>";

                        body = titleHTML + body;
                        body = "<html><head><style>img{width:100%}</style><script type='text/javascript'>function show(){window.demo.javaShow()} </script></head><body>" + body + "</body></html>";

                        replayCount = web.getReplyCount();
                        mHandler.sendEmptyMessage(INIT_SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSwipeBackLayout = this.getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public void onBackPressed() {
        if (hasFocus) {
            mWebView.requestFocus();

        } else {
            finish();
        }
    }

    @JavascriptInterface
    public void javaShow() {
        Intent intent = new Intent();
        intent.setClass(this, DetailImageActivity.class);
        intent.putExtra("image", images);
        startActivity(intent);
    }

    public void initWebView() {
        mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
        replayCountTextView.setText(String.valueOf(replayCount));
    }

    static class MyHandler extends Handler {

        WeakReference<DetailActivity> activity;

        public MyHandler(DetailActivity dActivity) {
            activity = new WeakReference(dActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            DetailActivity detailActivity = activity.get();
            if (detailActivity == null) {
                return;
            }
            detailActivity.initWebView();
        }
    }
}

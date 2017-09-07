package com.izdo.mynetease.splash.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.izdo.mynetease.MainActivity;
import com.izdo.mynetease.R;
import com.izdo.mynetease.service.DownloadImageService;
import com.izdo.mynetease.splash.OnTimeClickListener;
import com.izdo.mynetease.splash.TimeView;
import com.izdo.mynetease.splash.bean.Action;
import com.izdo.mynetease.splash.bean.Ads;
import com.izdo.mynetease.splash.bean.AdsDetail;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.HttpResponse;
import com.izdo.mynetease.util.HttpUtil;
import com.izdo.mynetease.util.ImageUtil;
import com.izdo.mynetease.util.JsonUtil;
import com.izdo.mynetease.util.Md5Helper;
import com.izdo.mynetease.util.SharedPrefrencesUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.izdo.mynetease.R.id.ads;

public class SplashActivity extends Activity {

    // 广告图片
    ImageView ads_img;

    private TimeView mTimeView;

    // 时长3秒
    int timeLength = 3 * 1000;
    // 刷新间隔250毫秒
    int space = 250;
    // 刷新总次数
    int frequency;
    // 当前已刷新次数
    int nowFrequency = 0;

    static final String JSON_CACHE = "ads_Json";
    static final String JSON_CACHE_TIME_OUT = "ads_Json_time_out";
    static final String JSON_CACHE_LAST_SUCCESS = "ads_Json_last_success";
    static final String LAST_IMAGE_INDEX = "img_index";

    Handler mHandler;

    private Runnable NoPhotoGoToMain = new Runnable() {
        @Override
        public void run() {
            goToMain();
        }
    };

    Runnable refreshing = new Runnable() {
        @Override
        public void run() {
            // obtainMessage() 从消息池中复用
            Message message = mHandler.obtainMessage(0);
            message.arg1 = nowFrequency;
            mHandler.sendMessage(message);
            mHandler.postDelayed(this, space);
            nowFrequency++;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 开启全屏设置
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 沉浸式
        // View decorView = getWindow().getDecorView();
        // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_splash);

        // 权限请求
        requestPermission();

        initView();

        getAds();

        showImage();

    }

    public void initView() {
        ads_img = (ImageView) findViewById(ads);

        mTimeView = (TimeView) findViewById(R.id.timeView);
        mTimeView.setVisibility(View.GONE);
        mTimeView.setListener(new OnTimeClickListener() {
            @Override
            public void onClickTime(View view) {
                // 点击之后直接跳转到MainActivity，并把定时去除
                mHandler.removeCallbacks(refreshing);
                goToMain();
            }
        });

        // 总次数 = 总时长 / 间隔
        frequency = timeLength / space;

        mHandler = new MyHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(refreshing);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(refreshing);
    }

    public void goToMain() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 权限申请 6.0以上才需申请
    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            httpRequest();
        }
    }

    //获取权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show();
                    httpRequest();
                } else {
                    Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    // 判断是否需要发送http请求
    public void getAds() {
        String cache = SharedPrefrencesUtil.getString(this, JSON_CACHE);
        if (TextUtils.isEmpty(cache)) {
            httpRequest();
        } else {

            int time_out = SharedPrefrencesUtil.getInt(this, JSON_CACHE_TIME_OUT);
            long now = System.currentTimeMillis();
            long last = SharedPrefrencesUtil.getLong(this, JSON_CACHE_LAST_SUCCESS);

            if ((now - last) > time_out * 60 * 1000) {
                httpRequest();
            }
        }
    }

    // 获取广告数据
    public void httpRequest() {
        HttpUtil util = HttpUtil.getInstance();
        util.getDate(Constant.SPLASH_URL, new HttpResponse<String>(String.class) {
            @Override
            public void onError(String msg) {
                Logger.i(msg);
            }

            @Override
            public void onSucceed(String json) {
                Ads ads = JsonUtil.parseJson(json, Ads.class);

                if (ads != null) {
                    // 请求成功

                    // 缓存json
                    SharedPrefrencesUtil.saveString(SplashActivity.this, JSON_CACHE, json);
                    //  缓存超时时间
                    SharedPrefrencesUtil.saveInt(SplashActivity.this, JSON_CACHE_TIME_OUT, ads.getNext_req());
                    //  缓存上次请求成功的时间
                    SharedPrefrencesUtil.saveLong(SplashActivity.this, JSON_CACHE_LAST_SUCCESS, System.currentTimeMillis());

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, DownloadImageService.class);
                    intent.putExtra(DownloadImageService.ADS_DATE, ads);
                    startService(intent);
                }
            }
        });
    }

    // 缓存 -> 对象 -> url -> MD5 -> 找到图片 -> 显示图片
    public void showImage() {
        // 读出缓存
        String cache = SharedPrefrencesUtil.getString(this, JSON_CACHE);

        if (!TextUtils.isEmpty(cache)) {

            //只有显示了广告图的情况下才显示倒数控件
            mTimeView.setVisibility(View.VISIBLE);
            mHandler.post(refreshing);

            // 读出上次显示图片的索引
            int index = SharedPrefrencesUtil.getInt(this, LAST_IMAGE_INDEX);

            // 转化成对象
            Ads ads = JsonUtil.parseJson(cache, Ads.class);
            int size = ads.getAds().size();

            if (ads == null)
                return;
            List<AdsDetail> adsDetails = ads.getAds();
            if (adsDetails != null && adsDetails.size() > 0) {
                final AdsDetail detail = adsDetails.get(index % size);
                List<String> urls = detail.getRes_url();
                if (urls != null && !TextUtils.isEmpty(urls.get(0))) {
                    // 获取url
                    String url = urls.get(0);
                    // 计算文件名
                    String image_name = Md5Helper.toMD5(url);

                    File image = ImageUtil.getFileByName(image_name);
                    if (image.exists()) {
                        Bitmap bitmap = ImageUtil.getImageBitMapByFile(image);
                        ads_img.setImageBitmap(bitmap);
                        index++;
                        SharedPrefrencesUtil.saveInt(this, LAST_IMAGE_INDEX, index);

                        ads_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Action action = detail.getAction_params();
                                mHandler.removeCallbacks(refreshing);
                                if (action != null && !TextUtils.isEmpty(action.getLink_url())) {
                                    Intent intent = new Intent();
                                    intent.setClass(SplashActivity.this, WebViewActivity.class);
                                    intent.putExtra(WebViewActivity.ACTION_NAME, action);
                                    startActivity(intent);
                                    // 关闭当前页面
                                    finish();
                                    mHandler.removeCallbacks(refreshing);
                                }
                            }
                        });
                    }
                }
            }
        } else {
            mHandler.postDelayed(NoPhotoGoToMain, 3000);
        }
    }

    // 使用静态内部类切断访问activity 防止内存泄露
    static class MyHandler extends Handler {

        // 使用弱引用持有对象
        WeakReference<SplashActivity> activity;

        public MyHandler(SplashActivity act) {
            this.activity = new WeakReference<SplashActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {

            //获取对象
            SplashActivity act = activity.get();

            if (act == null) {
                return;
            }

            switch (msg.what) {
                case 0:
                    int nowFrequency = msg.arg1;
                    if (nowFrequency <= act.frequency) {
                        act.mTimeView.setProgress(act.frequency, nowFrequency);
                    } else {
                        act.mHandler.removeCallbacks(act.refreshing);
                        act.goToMain();
                    }
                    break;
            }
        }

    }
}

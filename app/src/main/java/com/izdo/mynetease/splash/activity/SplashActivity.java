package com.izdo.mynetease.splash.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.izdo.mynetease.R;
import com.izdo.mynetease.service.DownloadImageService;
import com.izdo.mynetease.splash.bean.Action;
import com.izdo.mynetease.splash.bean.Ads;
import com.izdo.mynetease.splash.bean.AdsDetail;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.ImageUtil;
import com.izdo.mynetease.util.JsonUtil;
import com.izdo.mynetease.util.Md5Helper;
import com.izdo.mynetease.util.SharedPrefrencesUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.izdo.mynetease.R.id.ads;

public class SplashActivity extends Activity {

    // 广告图片
    ImageView ads_img;

    static final String JSON_CACHE = "ads_Json";
    static final String JSON_CACHE_TIME_OUT = "ads_Json_time_out";
    static final String JSON_CACHE_LAST_SUCCESS = "ads_Json_last_success";
    static final String LAST_IMAGE_INDEX = "img_index";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 开启全屏设置
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        requestPermission();

        ads_img = (ImageView) findViewById(ads);

        getAds();

        showImage();

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
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constant.SPLASH_URL)
                .build();

        // 开启一个异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 请求失败
                }

                // 获取接口的数据
                String date = response.body().string();

                Ads ads = JsonUtil.parseJson(date, Ads.class);

                if (ads != null) {
                    // 请求成功

                    // 缓存json
                    SharedPrefrencesUtil.saveString(SplashActivity.this, JSON_CACHE, date);
                    //  缓存超时时间
                    SharedPrefrencesUtil.saveInt(SplashActivity.this, JSON_CACHE_TIME_OUT, ads.getNext_req());
                    //  缓存上次请求成功的时间
                    SharedPrefrencesUtil.saveLong(SplashActivity.this, JSON_CACHE_LAST_SUCCESS, System.currentTimeMillis());

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, DownloadImageService.class);
                    intent.putExtra(DownloadImageService.ADS_DATE, ads);
                    startService(intent);

                } else {
                    // 请求失败
                }
            }
        });
    }

    // 缓存 -> 对象 -> url -> MD5 -> 找到图片 -> 显示图片
    public void showImage() {
        // 读出缓存
        String cache = SharedPrefrencesUtil.getString(this, JSON_CACHE);
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
                            Action action =  detail.getAction_params();
                            if (action != null && !TextUtils.isEmpty(action.getLink_url())) {
                                Intent intent = new Intent();
                                intent.setClass(SplashActivity.this, WebViewActivity.class);
                                intent.putExtra(WebViewActivity.ACTION_NAME, action);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }
}

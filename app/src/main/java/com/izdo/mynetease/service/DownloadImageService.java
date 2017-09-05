package com.izdo.mynetease.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.izdo.mynetease.splash.bean.Ads;
import com.izdo.mynetease.splash.bean.AdsDetail;
import com.izdo.mynetease.util.Constant;
import com.izdo.mynetease.util.ImageUtil;
import com.izdo.mynetease.util.Md5Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by iZdo on 2017/9/4.
 */

public class DownloadImageService extends IntentService {

    public static final String ADS_DATE = "ads";

    // 必须实现的默认构造方法
    public DownloadImageService() {
        super("DownloadImageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // 接收到http请求的对象
        Ads ads = (Ads) intent.getSerializableExtra(ADS_DATE);

        // 下载图片
        List<AdsDetail> list = ads.getAds();
        for (int i = 0; i < list.size(); i++) {
            AdsDetail detail = list.get(i);
            List<String> imgs = detail.getRes_url();

            if (imgs != null) {
                String img_url = imgs.get(0);
                if (!TextUtils.isEmpty(img_url)) {
                    // 图片地址转成唯一的MD5文件名
                    String cache_name = Md5Helper.toMD5(img_url);
                    // 先判断图片是是否存在，如果存在则不下载
                    if (!ImageUtil.checkImageIsDownLoad(cache_name)) {
                        // 下载图片
                        downloadImage(img_url, cache_name);
                    }
                }
            }
        }
    }

    public void downloadImage(String url, String MD5_name) {

        try {
            URL uri = new URL(url);
            URLConnection urlConnection = uri.openConnection();
            // 获取一张图片
            Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            // 在sd卡上写入图片
            saveToSD(bitmap, MD5_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToSD(Bitmap bitmap, String MD5_name) {
        if (bitmap == null) {
            return;
        }

        // 判断手机sd卡是否装载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File SD = Environment.getExternalStorageDirectory();
            File cacheFile = new File(SD, Constant.CACHE);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }

            File image = new File(cacheFile, MD5_name + ".jpg");

            // 如果图片存在
            if (image.exists()) {
                return;
            }

            try {
                FileOutputStream image_out = new FileOutputStream(image);

                // bitmap压缩到sd卡
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, image_out);
                image_out.flush();
                image_out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

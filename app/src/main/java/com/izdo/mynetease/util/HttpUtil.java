package com.izdo.mynetease.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by iZdo on 2017/9/7.
 */

public class HttpUtil {

    static HttpUtil util;
    static OkHttpClient client;

    private HttpUtil() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // 单例
    public static HttpUtil getInstance() {
        if (util == null) {
            synchronized (HttpUtil.class) {
                if (util == null) {
                    util = new HttpUtil();
                }
            }
        }
        return util;
    }

    public void getData(String url, final HttpResponse httpResponse) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        // 开启一个异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                httpResponse.onError("连接服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 请求失败
                    httpResponse.onError("连接服务器失败");
                    return;
                }
                // 获取接口的数据
                String date = response.body().string();
                httpResponse.parse(date);
            }
        });
    }
}

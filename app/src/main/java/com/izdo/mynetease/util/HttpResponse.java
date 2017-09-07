package com.izdo.mynetease.util;

import android.text.TextUtils;

/**
 * Created by iZdo on 2017/9/7.
 */

public abstract class HttpResponse<T> {

    // http返回的类型
    Class<T> t;

    public HttpResponse(Class<T> t) {
        this.t = t;
    }

    // 失败->调用者->失败的原因
    public abstract void onError(String msg);
    // 成功->返回需要的类型
    public abstract  void onSucceed(T t);

    public void parse(String json){
        if(TextUtils.isEmpty(json)){
            // 请求失败
            onError("连接网络失败");
            return ;
        }

        // 只需要json数据 不需要转化
        if (t == String.class) {
            onSucceed((T) json);
            return ;
        }

        // 尝试转化->需要的类型
        T result = JsonUtil.parseJson(json,t);

        // 解析成功
        if(result!=null){
            onSucceed(result);
        }else{
            onError("json解析失败");
        }
    }
}

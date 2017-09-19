package com.izdo.mynetease.news.bean;

/**
 * Created by iZdo on 2017/9/14.
 */

public class ShowTabEvent {
    boolean isShow = false;

    public ShowTabEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}

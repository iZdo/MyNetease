package com.izdo.mynetease.splash.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iZdo on 2017/9/4.
 */

public class Ads implements Serializable {

    private List<AdsDetail> ads;
    private int next_req;

    public List<AdsDetail> getAds() {
        return ads;
    }

    public void setAds(List<AdsDetail> ads) {
        this.ads = ads;
    }

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "ads=" + ads +
                ", next_req=" + next_req +
                '}';
    }
}

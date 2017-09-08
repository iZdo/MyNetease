package com.izdo.mynetease.news.bean;

import java.util.List;

/**
 * Created by iZdo on 2017/9/7.
 */

public class HotDetail {

    private List<Banner> ads;
    private String img;
    private String title;
    String source;
    private int replyCount;
    private String specialID;

    public List<Banner> getAds() {
        return ads;
    }

    public void setAds(List<Banner> ads) {
        this.ads = ads;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    @Override
    public String toString() {
        return "HotDetail{" +
                "ads=" + ads +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", replyCount=" + replyCount +
                ", specialID='" + specialID + '\'' +
                '}';
    }
}

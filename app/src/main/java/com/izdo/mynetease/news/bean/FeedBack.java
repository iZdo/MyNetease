package com.izdo.mynetease.news.bean;

/**
 * Created by iZdo on 2017/9/12.
 */

public class FeedBack {

    // 头像
    private String timg;
    // 来源
    private String f;
    // 点赞数量
    private String v;
    // 时间
    private String t;
    // 内容
    private String b;
    private String fi;
    // 名称
    private String n;
    private String l;
    // 是否是vip
    private String vip;
    private int index;

    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "timg='" + timg + '\'' +
                ", f='" + f + '\'' +
                ", v='" + v + '\'' +
                ", t='" + t + '\'' +
                ", b='" + b + '\'' +
                ", fi='" + fi + '\'' +
                ", n='" + n + '\'' +
                ", l='" + l + '\'' +
                ", vip='" + vip + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}

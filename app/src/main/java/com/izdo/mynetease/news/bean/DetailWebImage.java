package com.izdo.mynetease.news.bean;

import java.io.Serializable;

/**
 * Created by iZdo on 2017/9/11.
 */

public class DetailWebImage implements Serializable {

    private String alt;
    private String pixel;
    private String ref;
    private String src;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "DetailWebImage{" +
                "alt='" + alt + '\'' +
                ", pixel='" + pixel + '\'' +
                ", ref='" + ref + '\'' +
                ", src='" + src + '\'' +
                '}';
    }
}

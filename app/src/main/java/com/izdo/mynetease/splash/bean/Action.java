package com.izdo.mynetease.splash.bean;

import java.io.Serializable;

/**
 * Created by iZdo on 2017/9/4.
 */

public class Action implements Serializable{

    public String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "Action{" +
                "link_url='" + link_url + '\'' +
                '}';
    }
}

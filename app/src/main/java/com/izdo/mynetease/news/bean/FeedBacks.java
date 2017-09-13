package com.izdo.mynetease.news.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by iZdo on 2017/9/12.
 */

public class FeedBacks {
    ArrayList<FeedBack> hot;

    boolean isTitle = false;

    String title;

    public FeedBacks() {
        this.hot = new ArrayList<>();
    }

    public ArrayList<FeedBack> getHot() {
        return hot;
    }

    public void setHot(ArrayList<FeedBack> hot) {
        this.hot = hot;
    }

    public boolean getIsTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void add(FeedBack feedBack) {
        hot.add(feedBack);
    }

    public void sort() {
        Collections.sort(hot, new FeedBackSort());
    }

    class FeedBackSort implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            if (((FeedBack) t1).getIndex() > ((FeedBack) t2).getIndex()) {
                return 1;
            } else if (((FeedBack) t1).getIndex() == ((FeedBack) t2).getIndex()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public FeedBack getLastData() {
        return hot.get(hot.size() - 1);
    }

    @Override
    public String toString() {
        return "FeedBacks{" +
                "hot=" + hot +
                ", isTitle=" + isTitle +
                ", title='" + title + '\'' +
                '}';
    }
}

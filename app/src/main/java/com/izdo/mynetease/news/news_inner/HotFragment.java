package com.izdo.mynetease.news.news_inner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izdo.mynetease.R;

/**
 * Created by iZdo on 2017/9/6.
 */

public class HotFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

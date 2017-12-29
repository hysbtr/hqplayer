package com.hysbtr.hqplayer.controller;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by guoxiaodong on 2017/10/10 16:00
 */
public interface ILayoutAttachDetach {
    void attachViewToParent(View child, int index, ViewGroup.LayoutParams params);

    void detachViewFromParent(int index);
}

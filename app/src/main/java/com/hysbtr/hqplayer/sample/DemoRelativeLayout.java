package com.hysbtr.hqplayer.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hysbtr.hqplayer.controller.ILayoutAttachDetach;

/**
 * Created by guoxiaodong on 2017/10/10 15:47
 */
public class DemoRelativeLayout extends RelativeLayout implements ILayoutAttachDetach {
    public DemoRelativeLayout(Context context) {
        super(context);
    }

    public DemoRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);
    }

    @Override
    public void detachViewFromParent(int index) {
        super.detachViewFromParent(index);
    }
}

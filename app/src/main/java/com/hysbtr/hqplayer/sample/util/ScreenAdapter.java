package com.hysbtr.hqplayer.sample.util;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hysbtr.hqplayer.sample.DemoApplication;

public class ScreenAdapter {
    public static final int WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT;
    public static final int MATCH = RelativeLayout.LayoutParams.MATCH_PARENT;
    private static final int defaultWidth = 1920;
    private static final int defaultHeight = 1080;
    public static int screenWidth;
    public static int screenHeight;
    public static float scaledDensity;

    private ScreenAdapter() {
    }

    public static void init() {
        DisplayMetrics metrics = DemoApplication.getInstance().getResources().getDisplayMetrics();
        scaledDensity = metrics.scaledDensity;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels == 672 ? 720 : metrics.heightPixels == 1008 ? 1080 : metrics.heightPixels;
    }

    /**
     * 字体适配(只适配TextView及TextView的子类)
     */
    public static void scaleTxtSize(View v, float size) {
        if (v instanceof TextView) {
            float txtSize = size / Math.min(defaultWidth, defaultHeight) * Math.min(screenWidth, screenHeight) / scaledDensity;
            ((TextView) v).setTextSize(txtSize);
        }
    }

    /**
     * 控件适配(动态添加的控件要先自己添加LayoutParams)
     */
    public static void scaleView(View v, int w, int h, int l, int t, int r, int b) {
        if (WRAP != w && MATCH != w) {
            w = scaleX(w);
        }
        if (WRAP != h && MATCH != h) {
            h = scaleY(h);
        }
        l = scaleX(l);
        t = scaleY(t);
        r = scaleX(r);
        b = scaleY(b);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (params != null) {
            params.width = w;
            params.height = h;
            params.setMargins(l, t, r, b);
        }
    }

    /**
     * 控件适配(动态添加的控件要先自己添加LayoutParams)
     */
    public static void scaleView(View v, int w, int h, int l, int t) {
        scaleView(v, w, h, l, t, 0, 0);
    }

    /**
     * 控件适配(动态添加的控件要先自己添加LayoutParams)
     */
    public static void scaleView(View v, int w, int h) {
        scaleView(v, w, h, 0, 0);
    }

    /**
     * @return !=0
     */
    public static int scaleX(int x) {
        int scaleX = Math.round(x * screenWidth / (float) defaultWidth);

        if (scaleX == 0 && x != 0) {
            scaleX = x < 0 ? -1 : 1;
        }

        return scaleX;
    }

    /**
     * @return !=0
     */
    public static int scaleY(int y) {
        int scaleY = Math.round(y * screenHeight / (float) defaultHeight);

        if (scaleY == 0 && y != 0) {
            scaleY = y < 0 ? -1 : 1;
        }

        return scaleY;
    }

}

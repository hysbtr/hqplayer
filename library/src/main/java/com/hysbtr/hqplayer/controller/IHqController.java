package com.hysbtr.hqplayer.controller;

import android.view.Surface;

import com.hysbtr.hqplayer.core.HqPlayer;
import com.hysbtr.hqplayer.core.IHqPlayer;

/**
 * Created by guoxiaodong on 2017/6/5
 * player和videoView间的桥梁
 */
public interface IHqController {
    Surface getSurface();

    void onPlayerAttached(IHqPlayer hqPlayer);

    void onPlayerDetached();

    void onPlayerStatusChanged(@HqPlayer.HqPlayerStatus int playerStatus);
}
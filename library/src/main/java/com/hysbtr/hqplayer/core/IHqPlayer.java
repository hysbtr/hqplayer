package com.hysbtr.hqplayer.core;

import com.hysbtr.hqplayer.controller.IHqController;

/**
 * Created by guoxiaodong on 2017/10/19 11:15
 * hq播放器拓展接口，补充通用播放器接口功能
 */
public interface IHqPlayer extends IPlayer {
    void changeHqPlayerStatus(@HqPlayer.HqPlayerStatus int playerStatus);

    @HqPlayer.HqPlayerStatus
    int getHqPlayerStatus();

    void attachToHqPlayer(IHqController hqController);

    void detachFromHqPlayer();

    boolean isBaseHqPlayerStatus(int basePlayerStatus);

    void switchPlayer();

    @HqPlayer.PlayerCodecType
    int getPlayerCodecType();

    @HqPlayer.HqPlayerType
    int getPlayerType();
}

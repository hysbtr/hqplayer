package com.hysbtr.hqplayer.constant;

/**
 * Created by guoxiaodong on 2017/3/27
 * 第一位:基本播放状态
 * 第二位:暂时不用
 * 第三位:暂时不用
 * 第四位:播放子状态
 */
public interface HqPlayerStatus {
    int PLAYER_STATE_IDLE = 0x1000;
    int PLAYER_STATE_PREPARE = 0x2000;
    int PLAYER_STATE_PLAYING = 0x3000;
    int PLAYER_STATE_PLAYING_CLEAR = 0x3001;
    int PLAYER_STATE_PLAYING_SHOW = 0x3002;
    int PLAYER_STATE_PAUSE = 0x4000;
    int PLAYER_STATE_SEEK = 0x5000;
    int PLAYER_STATE_SEEK_CLEAR = 0x5001;
    int PLAYER_STATE_SEEK_SHOW = 0x5002;
    int PLAYER_STATE_STOP = 0x6000;
    int PLAYER_STATE_COMPLETION = 0x7000;
}
package com.hysbtr.hqplayer;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.hysbtr.hqplayer.core.HqPlayer;

import java.io.IOException;

import static com.hysbtr.hqplayer.constant.HqPlayerType.EXO_PLAYER;
import static com.hysbtr.hqplayer.constant.PlayerCodecType.CODEC_SOFT;

/**
 * Created by guoxiaodong on 2017/6/5
 * 目前播放器是单例的
 */
public class HqMediaPlayerManager {
    private HqPlayer hqPlayer;
    private int loopCount = 1;
    @HqPlayer.HqPlayerType
    private int playerType = EXO_PLAYER;
    @HqPlayer.PlayerCodecType
    private int playerCodecType = CODEC_SOFT;
    @HqPlayer.HqPlayerType
    private int previousPlayerType;
    private boolean isShowHqPlayerInfo;
    private Integer frameDrop;
    private boolean isSurfaceView;

    private HqMediaPlayerManager() {
    }

    public static HqMediaPlayerManager getInstance() {
        return Holder.instance;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    @HqPlayer.HqPlayerType
    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(@HqPlayer.HqPlayerType int playerType) {
        this.playerType = playerType;
    }

    @HqPlayer.PlayerCodecType
    public int getPlayerCodecType() {
        return playerCodecType;
    }

    public void setPlayerCodecType(@HqPlayer.PlayerCodecType int playerCodecType) {
        this.playerCodecType = playerCodecType;
    }

    public boolean isShowHqPlayerInfo() {
        return isShowHqPlayerInfo;
    }

    public void setShowHqPlayerInfo(boolean showHqPlayerInfo) {
        isShowHqPlayerInfo = showHqPlayerInfo;
    }

    public Integer getFrameDrop() {
        return frameDrop;
    }

    public void setFrameDrop(Integer frameDrop) {
        this.frameDrop = frameDrop;
    }

    public boolean isSurfaceView() {
        return isSurfaceView;
    }

    public void setSurfaceView(boolean surfaceView) {
        isSurfaceView = surfaceView;
    }

    public HqPlayer getMediaPlayer(Context context, String videoUrl) throws IOException {
        if (TextUtils.isEmpty(videoUrl)) {
            return null;
        }
        if (hqPlayer == null) {
            hqPlayer = new HqPlayer(context);
            hqPlayer.setDataSource(videoUrl);
            previousPlayerType = playerType;
        } else {
            switchPlayer(videoUrl);

            String oldVideoUrl = hqPlayer.getDataSource();
            Uri oldVideoUri = Uri.parse(oldVideoUrl);
            Uri videoUri = Uri.parse(videoUrl);
            if (!oldVideoUri.getPath().equals(videoUri.getPath())) {
                hqPlayer.stop();// TODO: 2017/10/12 是否冗余，有待检查
            }
        }
        return hqPlayer;
    }

    public void switchPlayer(String videoUrl) throws IOException {
        if (previousPlayerType != playerType) {
            hqPlayer.switchPlayer();
            hqPlayer.setDataSource(videoUrl);
            previousPlayerType = playerType;
        }
    }

    private static class Holder {
        private static HqMediaPlayerManager instance = new HqMediaPlayerManager();
    }
}
package com.hysbtr.hqplayer.core;

import android.view.Surface;

import java.io.IOException;

/**
 * Created by guoxiaodong on 2017/10/19 10:45
 * 对外暴露的播放器通用接口，屏蔽具体播放器
 */
public interface IPlayer {
    String getDataSource();

    void setDataSource(String path) throws IOException;

    void prepareAsync();

    void start();

    void pause();

    void stop();

    void seekTo(long posMsc);

    void reset();

    void release();

    void setSurface(Surface surface);

    boolean isPlaying();

    int getVideoWidth();

    int getVideoHeight();

    long getCurrentPosition();

    long getDuration();

    void setOnPreparedListener(IPlayerStatusListener.OnPreparedListener onPreparedListener);

    void setOnCompletionListener(IPlayerStatusListener.OnCompletionListener onCompletionListener);

    void setOnSeekToListener(IPlayerStatusListener.OnSeekToListener onSeekToListener);
}

package com.hysbtr.hqplayer.player;

import android.media.AudioManager;
import android.util.Log;
import android.view.Surface;

import com.hysbtr.hqplayer.HqMediaPlayerManager;
import com.hysbtr.hqplayer.constant.PlayerCodecType;
import com.hysbtr.hqplayer.core.HqPlayer;
import com.hysbtr.hqplayer.core.IPlayer;
import com.hysbtr.hqplayer.core.IPlayerStatusListener;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by guoxiaodong on 2017/10/19 10:56
 * bilibili的ijk播放器的代理类
 */
public class IjkPlayer implements
        IPlayer,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnInfoListener {
    private static final String TAG = IjkPlayer.class.getSimpleName();
    private IjkMediaPlayer ijkPlayer;
    private IPlayerStatusListener.OnPreparedListener onPreparedListener;
    private IPlayerStatusListener.OnSeekToListener onSeekToListener;
    private IPlayerStatusListener.OnCompletionListener onCompletionListener;

    public IjkPlayer() {
        Log.i(TAG, "create IjkPlayer");
        ijkPlayer = new IjkMediaPlayer();
        ijkPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ijkPlayer.setOnPreparedListener(this);
        ijkPlayer.setOnCompletionListener(this);
        ijkPlayer.setOnInfoListener(this);
    }

    @Override
    public String getDataSource() {
        return ijkPlayer.getDataSource();
    }

    @Override
    public void setDataSource(String path) throws IOException {
        if (HqMediaPlayerManager.getInstance().getLoopCount() > 1) {
            ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "loop", HqMediaPlayerManager.getInstance().getLoopCount());
        }
        if (HqMediaPlayerManager.getInstance().getPlayerCodecType() == PlayerCodecType.CODEC_HARD) {
            ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-avc", 1);
        }
        Integer frameDrop = HqMediaPlayerManager.getInstance().getFrameDrop();
        if (frameDrop != null && frameDrop >= -1 && frameDrop <= 120) {
            ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", frameDrop);
        }
        ijkPlayer.setDataSource(path);
    }

    @Override
    public void prepareAsync() {
        ijkPlayer.prepareAsync();
    }

    @Override
    public void start() {
        ijkPlayer.start();
    }

    @Override
    public void pause() {
        ijkPlayer.pause();
    }

    @Override
    public void stop() {
        ijkPlayer.stop();
    }

    @Override
    public void seekTo(long posMsc) {
        ijkPlayer.seekTo(posMsc);
    }

    @Override
    public void reset() {
        ijkPlayer.reset();
    }

    @Override
    public void release() {
        ijkPlayer.release();
    }

    @Override
    public void setSurface(Surface surface) {
        ijkPlayer.setSurface(surface);
    }

    @Override
    public boolean isPlaying() {
        return ijkPlayer.isPlaying();
    }

    @Override
    public int getVideoWidth() {
        return ijkPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return ijkPlayer.getVideoHeight();
    }

    @Override
    public long getCurrentPosition() {
        return ijkPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkPlayer.getDuration();
    }

    @Override
    public void setOnPreparedListener(IPlayerStatusListener.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    @Override
    public void setOnCompletionListener(IPlayerStatusListener.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    @Override
    public void setOnSeekToListener(IPlayerStatusListener.OnSeekToListener onSeekToListener) {
        this.onSeekToListener = onSeekToListener;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(this);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(this);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int i1) {
        if (onSeekToListener != null) {
            onSeekToListener.onSeekTo(this, what);
        }
        return false;
    }

    @HqPlayer.PlayerCodecType
    public int getVideoDecoder() {
        int videoDecoder = ijkPlayer.getVideoDecoder();
        switch (videoDecoder) {
            case IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC:
                return PlayerCodecType.CODEC_SOFT;
            case IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC:
                return PlayerCodecType.CODEC_HARD;
            default:
                return PlayerCodecType.CODE_UNKNOWN;
        }
    }
}

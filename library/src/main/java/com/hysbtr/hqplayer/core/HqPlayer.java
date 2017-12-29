package com.hysbtr.hqplayer.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.Surface;

import com.hysbtr.hqplayer.HqMediaPlayerManager;
import com.hysbtr.hqplayer.controller.IHqController;
import com.hysbtr.hqplayer.player.ExoPlayer;
import com.hysbtr.hqplayer.player.IjkPlayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_COMPLETION;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_IDLE;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PREPARE;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_SEEK_CLEAR;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_SEEK_SHOW;
import static com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_STOP;
import static com.hysbtr.hqplayer.constant.HqPlayerType.EXO_PLAYER;
import static com.hysbtr.hqplayer.constant.HqPlayerType.IJK_PLAYER;
import static com.hysbtr.hqplayer.constant.HqPlayerType.UNKNOWN_PLAYER;
import static com.hysbtr.hqplayer.constant.PlayerCodecType.CODEC_HARD;
import static com.hysbtr.hqplayer.constant.PlayerCodecType.CODEC_SOFT;
import static com.hysbtr.hqplayer.constant.PlayerCodecType.CODE_UNKNOWN;

/**
 * Created by guoxiaodong on 2017/10/19 11:13
 * hq播放器的代理类
 */
public final class HqPlayer implements
        IHqPlayer,
        IPlayerStatusListener.OnPreparedListener,
        IPlayerStatusListener.OnSeekToListener,
        IPlayerStatusListener.OnCompletionListener {
    private static final String TAG = HqPlayer.class.getSimpleName();
    private IPlayer player;
    private WeakReference<IHqController> hqControllerRef;
    @HqPlayerStatus
    private int playerStatus = PLAYER_STATE_IDLE;
    private Context context;

    public HqPlayer(Context context) {
        this.context = context.getApplicationContext();
        switchPlayer();
    }

    @Override
    public void changeHqPlayerStatus(@HqPlayerStatus int playerStatus) {
        this.playerStatus = playerStatus;
        IHqController hqController = getHqController();
        if (hqController != null) {
            hqController.onPlayerStatusChanged(playerStatus);
        }
    }

    public IHqController getHqController() {
        return null == hqControllerRef ? null : hqControllerRef.get();
    }

    @Override
    @HqPlayerStatus
    public int getHqPlayerStatus() {
        return playerStatus;
    }

    @Override
    public void attachToHqPlayer(IHqController hqController) {
        if (hqController == null) {
            return;
        }

        detachFromHqPlayer();

        // efdContainer is not null
        hqControllerRef = new WeakReference<>(hqController);
        hqController.onPlayerAttached(this);

        Surface surface = hqController.getSurface();
        if (null != surface) {
            setSurface(surface);
        }
    }

    @Override
    public void detachFromHqPlayer() {
        setSurface(null);
        IHqController hqController = getHqController();
        if (hqController != null) {
            hqController.onPlayerDetached();
        }
    }

    @Override
    public boolean isBaseHqPlayerStatus(int basePlayerStatus) {
        return (playerStatus & 0xF000) == basePlayerStatus;
    }

    @Override
    public void switchPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
        switch (HqMediaPlayerManager.getInstance().getPlayerType()) {
            case EXO_PLAYER:
                player = new ExoPlayer(context);
                break;
            case IJK_PLAYER:
                player = new IjkPlayer();
                break;
            default:
                player = new ExoPlayer(context);
                break;
        }
        player.setOnPreparedListener(this);
        player.setOnSeekToListener(this);
        player.setOnCompletionListener(this);
        playerStatus = PLAYER_STATE_IDLE;
    }

    @Override
    public String getDataSource() {
        return player.getDataSource();
    }

    @Override
    public void setDataSource(String path) {
        try {
            player.setDataSource(path);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.setDataSource-->", e);
        }
    }

    @Override
    public void prepareAsync() {
        try {
            player.prepareAsync();
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PREPARE);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.prepareAsync-->", e);
        }
    }

    @Override
    public void start() {
        try {
            player.start();
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.start-->", e);
        }
    }

    @Override
    public void pause() {
        try {
            player.pause();
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.pause-->", e);
        }
    }

    @Override
    public void stop() {
        try {
            player.stop();
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_STOP);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.stop-->", e);
        }
    }

    @Override
    public void seekTo(long posMsc) {
        try {
            player.seekTo(posMsc);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.seekTo-->", e);
        }
    }

    @Override
    public void reset() {
        try {
            player.reset();
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.reset-->", e);
        }
    }

    @Override
    public void release() {
        try {
            player.release();
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.release-->", e);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        try {
            player.setSurface(surface);
        } catch (Exception e) {
            Log.e(TAG, "HqPlayer.setSurface-->", e);
        }
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getVideoWidth() {
        return player.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return player.getVideoHeight();
    }

    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return player.getDuration();
    }

    @Override
    public void setOnPreparedListener(IPlayerStatusListener.OnPreparedListener onPreparedListener) {
        player.setOnPreparedListener(onPreparedListener);
    }

    @Override
    public void setOnCompletionListener(IPlayerStatusListener.OnCompletionListener onCompletionListener) {
        player.setOnCompletionListener(onCompletionListener);
    }

    @Override
    public void setOnSeekToListener(IPlayerStatusListener.OnSeekToListener onSeekToListener) {
        player.setOnSeekToListener(onSeekToListener);
    }

    @Override
    public void onPrepared(IPlayer player) {
        if (isBaseHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE)) {// preparing时设置了暂停,prepared时需要恢复pause状态
            pause();
        } else {
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR);
        }
    }

    @Override
    public void onSeekTo(IPlayer player, int what) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_SEEK_SHOW);
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (isBaseHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE)) {
                changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE);
            } else {
                if (isPlaying()) {
                    changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR);
                } else {// 暂停状态快进后,显示暂停界面
                    changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_PAUSE);
                }
            }
        }
    }

    @Override
    public void onCompletion(IPlayer player) {
        changeHqPlayerStatus(com.hysbtr.hqplayer.constant.HqPlayerStatus.PLAYER_STATE_COMPLETION);
    }

    @PlayerCodecType
    public int getPlayerCodecType() {
        if (player instanceof ExoPlayer) {
            return CODEC_HARD;
        } else if (player instanceof IjkPlayer) {
            return ((IjkPlayer) player).getVideoDecoder();
        }
        return CODE_UNKNOWN;
    }

    @HqPlayerType
    public int getPlayerType() {
        if (player instanceof ExoPlayer) {
            return EXO_PLAYER;
        } else if (player instanceof IjkPlayer) {
            return IJK_PLAYER;
        }
        return UNKNOWN_PLAYER;
    }

    @IntDef({PLAYER_STATE_IDLE,
            PLAYER_STATE_PREPARE,
            PLAYER_STATE_PLAYING_CLEAR,
            PLAYER_STATE_PLAYING_SHOW,
            PLAYER_STATE_PAUSE,
            PLAYER_STATE_SEEK_CLEAR,
            PLAYER_STATE_SEEK_SHOW,
            PLAYER_STATE_STOP,
            PLAYER_STATE_COMPLETION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HqPlayerStatus {
    }

    @IntDef({EXO_PLAYER, IJK_PLAYER, UNKNOWN_PLAYER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HqPlayerType {
    }

    @IntDef({CODEC_SOFT, CODEC_HARD, CODE_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerCodecType {
    }
}

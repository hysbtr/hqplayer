package com.hysbtr.hqplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hysbtr.hqplayer.HqMediaPlayerManager;
import com.hysbtr.hqplayer.constant.HqPlayerStatus;
import com.hysbtr.hqplayer.constant.HqPlayerType;
import com.hysbtr.hqplayer.constant.PlayerCodecType;
import com.hysbtr.hqplayer.core.HqPlayer;
import com.hysbtr.hqplayer.core.IHqPlayer;
import com.hysbtr.hqplayer.options.DisposableOption;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by guoxiaodong on 2017/10/10 09:52
 */
public abstract class BaseHqVideoView extends RelativeLayout implements
        IHqController,
        TextureView.SurfaceTextureListener,
        SurfaceHolder.Callback {
    protected static final int HANDLER_UPDATE_UI = 364;
    private static final int HANDLER_INVISIBLE_CONTROLLER = 651;
    protected IHqPlayer hqPlayer;
    protected View surfaceContainer;
    protected HqVideoViewHandler handler;
    private long onKeyDownTime;
    private boolean isKeyUp = true;
    private Surface surface;
    private DisposableOption<Integer> seekToPosOption;
    private TextView hqPlayerInfoTv;

    public BaseHqVideoView(Context context) {
        super(context);
        init();
    }

    public BaseHqVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseHqVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setKeepScreenOn(false);
    }

    @Override
    public Surface getSurface() {
        return surface;
    }

    @Override
    public void onPlayerAttached(IHqPlayer hqPlayer) {
        if (HqMediaPlayerManager.getInstance().isSurfaceView()) {
            surfaceContainer = new SurfaceView(getContext());
            ((SurfaceView) surfaceContainer).getHolder().addCallback(this);
        } else {
            surfaceContainer = new TextureView(getContext());
            ((TextureView) surfaceContainer).setSurfaceTextureListener(this);
        }
        addView(surfaceContainer, 0);

        this.hqPlayer = hqPlayer;
        if (handler == null) {
            handler = new HqVideoViewHandler(this);
        }

        if (hqPlayerInfoTv == null && HqMediaPlayerManager.getInstance().isShowHqPlayerInfo()) {
            hqPlayerInfoTv = new TextView(getContext());
            hqPlayerInfoTv.setBackgroundColor(Color.BLACK);
            hqPlayerInfoTv.setTextColor(Color.WHITE);
            hqPlayerInfoTv.setTextSize(30);
            addView(hqPlayerInfoTv);
        }
    }

    @Override
    public void onPlayerDetached() {
        if (surfaceContainer instanceof SurfaceView) {
            ((SurfaceView) surfaceContainer).getHolder().removeCallback(this);
        } else if (surfaceContainer instanceof TextureView) {
            ((TextureView) surfaceContainer).setSurfaceTextureListener(null);
        }
        removeView(surfaceContainer);
        surfaceContainer = null;

        this.hqPlayer = null;
        if (handler != null) {
            handler.removeMessages(HANDLER_UPDATE_UI);
            handler = null;
        }
    }

    @Override
    public void onPlayerStatusChanged(@HqPlayer.HqPlayerStatus int playerStatus) {
        if (handler != null) {
            handler.removeMessages(HANDLER_UPDATE_UI);
            handler.removeMessages(HANDLER_INVISIBLE_CONTROLLER);
        }
        switch (playerStatus) {
            case HqPlayerStatus.PLAYER_STATE_IDLE:
//                changeUiToIdle();
                break;
            case HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW:
                changeUiToPlayShow();
                if (handler != null) {
                    handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                    handler.sendEmptyMessageDelayed(HANDLER_INVISIBLE_CONTROLLER, 3000);
                }
                break;
            case HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR:
                changeUiToPlayClear();
                if (handler != null) {
                    handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                }

                if (hqPlayer != null && hqPlayerInfoTv != null) {
                    String hqPlayerInfo;
                    switch (hqPlayer.getPlayerType()) {
                        case HqPlayerType.EXO_PLAYER:
                            hqPlayerInfo = "默认播放器";
                            break;
                        case HqPlayerType.IJK_PLAYER:
                            if (hqPlayer.getPlayerCodecType() == PlayerCodecType.CODEC_SOFT) {
                                hqPlayerInfo = "软解播放器";
                            } else if (hqPlayer.getPlayerCodecType() == PlayerCodecType.CODEC_HARD) {
                                hqPlayerInfo = "硬解播放器";
                            } else {
                                hqPlayerInfo = "未知播放器";
                            }
                            break;
                        case HqPlayerType.UNKNOWN_PLAYER:
                            hqPlayerInfo = "未知播放器";
                            break;
                        default:
                            hqPlayerInfo = "未知播放器";
                            break;
                    }
                    hqPlayerInfoTv.setText(hqPlayerInfo);
                }
                break;
            case HqPlayerStatus.PLAYER_STATE_PAUSE:
                changeUiToPause();
                break;
            case HqPlayerStatus.PLAYER_STATE_SEEK_CLEAR:
                changeUiToSeekClear();
                break;
            case HqPlayerStatus.PLAYER_STATE_SEEK_SHOW:
                changeUiToSeekShow();
                break;
            case HqPlayerStatus.PLAYER_STATE_PREPARE:
                changeUiToPrepare();
                int seekToPos;
                if (seekToPosOption != null && (seekToPos = seekToPosOption.getAndReset()) > 0) {
                    hqPlayer.seekTo(seekToPos);
                }
                break;
            case HqPlayerStatus.PLAYER_STATE_STOP:
//                changeUiToIdle();
                break;
            case HqPlayerStatus.PLAYER_STATE_COMPLETION:
//                changeUiToCompletion();
                break;
        }
    }

    protected void onScreenTouchUp() {
        if (hqPlayer == null) {
            return;
        }
        int playerState = hqPlayer.getHqPlayerStatus();
        switch (playerState) {
            case HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR:
                hqPlayer.changeHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW);
                break;
            case HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW:
                hqPlayer.changeHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR);
                break;
            case HqPlayerStatus.PLAYER_STATE_SEEK_CLEAR:
                hqPlayer.changeHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_SEEK_SHOW);
                break;
            case HqPlayerStatus.PLAYER_STATE_SEEK_SHOW:
                hqPlayer.changeHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_SEEK_CLEAR);
                break;
            default:
                break;
        }
    }

    protected void changeUiToPlayClear() {
    }

    protected void changeUiToPlayShow() {
    }

    protected void changeUiToPrepare() {
    }

    protected void changeUiToSeekClear() {
    }

    protected void changeUiToSeekShow() {
    }

    protected void changeUiToPause() {
    }

    public void attachPlayerAndPlay(String videoUrl) {
        try {
            if (hqPlayer == null) {
                hqPlayer = HqMediaPlayerManager.getInstance().getMediaPlayer(getContext(), videoUrl);
                // 第一次绑定时，如果之前时暂停状态要恢复播放
                if (hqPlayer != null && hqPlayer.isBaseHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_PAUSE)) {
                    hqPlayer.start();
                }
            } else {
                HqMediaPlayerManager.getInstance().switchPlayer(videoUrl);
            }
            if (hqPlayer == null) {
                return;
            }

            hqPlayer.attachToHqPlayer(this);

            if (hqPlayer.isBaseHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_STOP)) {
                hqPlayer.reset();
                hqPlayer.setDataSource(videoUrl);
                hqPlayer.prepareAsync();
            } else if (hqPlayer.isBaseHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_IDLE)) {
                hqPlayer.prepareAsync();
            } else {
                if (hqPlayer.isBaseHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_COMPLETION)) {
                    hqPlayer.reset();
                    hqPlayer.setDataSource(videoUrl);
                    hqPlayer.prepareAsync();
                } else {
                    hqPlayer.changeHqPlayerStatus(hqPlayer.getHqPlayerStatus());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopVideo() {
        if (hqPlayer != null) {
            hqPlayer.stop();
        }
    }

    public void releaseVideo() {
        if (hqPlayer != null) {
            hqPlayer.release();
        }
    }

    public void resetVideo() {
        if (hqPlayer != null) {
            hqPlayer.reset();
        }
    }

    public void pauseVideo() {
        if (hqPlayer != null && !hqPlayer.isBaseHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_STOP)) {
            hqPlayer.pause();
        }
    }

    public void resumeVideo() {
        resumeVideo(null);
    }

    public void resumeVideo(String videoUrl) {
        if (hqPlayer != null) {
            hqPlayer.start();
        } else {
            attachPlayerAndPlay(videoUrl);
        }
    }

    public void seekTo() {
    }

    protected long getDuration() {
        if (hqPlayer != null) {
            return hqPlayer.getDuration();
        }
        return 0;
    }

    public int getVideoWidth() {
        if (hqPlayer != null) {
            return hqPlayer.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (hqPlayer != null) {
            return hqPlayer.getVideoHeight();
        }
        return 0;
    }

    public boolean isBasePlayerStatus(int playerStatus) {
        return hqPlayer != null && hqPlayer.isBaseHqPlayerStatus(playerStatus);
    }

    protected void onKeyDown() {
        isKeyUp = false;
        onKeyDownTime = System.currentTimeMillis();
        if (handler != null) {
            handler.removeMessages(HANDLER_UPDATE_UI);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);
        if (hqPlayer != null) {
            hqPlayer.setSurface(this.surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // igonre
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // ignore
    }

    /**
     * 每500毫秒回调一次，用于刷新进度等UI
     */
    protected void updateControllerUi(int progressIncrement) {
    }

    public void setSeekToPos(int seekToPos) {
        if (seekToPosOption == null) {
            seekToPosOption = new DisposableOption<>(seekToPos, -1);
        } else {
            seekToPosOption.set(seekToPos);
        }
    }

    @HqPlayer.HqPlayerType
    public int getPlayerType() {
        if (hqPlayer != null) {
            return hqPlayer.getPlayerType();
        }
        return HqPlayerType.UNKNOWN_PLAYER;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surface = holder.getSurface();
        if (hqPlayer != null) {
            hqPlayer.setSurface(this.surface);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public static class HqVideoViewHandler extends Handler {
        private WeakReference<BaseHqVideoView> hqVideoViewRef;

        HqVideoViewHandler(BaseHqVideoView hqVideoView) {
            this.hqVideoViewRef = new WeakReference<>(hqVideoView);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseHqVideoView hqVideoView = hqVideoViewRef.get();
            if (hqVideoView == null) {
                return;
            }
            switch (msg.what) {
                case HANDLER_UPDATE_UI:
                    int increment = 15;
                    if (!hqVideoView.isKeyUp) {
                        long longPressTime = System.currentTimeMillis() - hqVideoView.onKeyDownTime;
                        if (longPressTime > 5000) {
                            increment = 60;
                        } else if (longPressTime > 2000) {
                            increment = 30;
                        }
                    }
                    hqVideoView.updateControllerUi(increment);
                    sendEmptyMessageDelayed(HANDLER_UPDATE_UI, 500);
                    break;
                case HANDLER_INVISIBLE_CONTROLLER:
                    if (hqVideoView.hqPlayer != null && hqVideoView.hqPlayer.getHqPlayerStatus() == HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW) {
                        hqVideoView.hqPlayer.changeHqPlayerStatus(HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

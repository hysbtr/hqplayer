package com.hysbtr.hqplayer.sample.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hysbtr.hqplayer.constant.HqPlayerStatus;
import com.hysbtr.hqplayer.controller.HqVideoView;
import com.hysbtr.hqplayer.core.HqPlayer;
import com.hysbtr.hqplayer.sample.R;
import com.hysbtr.hqplayer.sample.util.ScreenAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guoxiaodong on 2017/6/8
 */
public class ShortVideoView extends HqVideoView implements
        View.OnTouchListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {
    private ImageView playStatusIv;
    private TextView playStatusTv;
    private SeekBar seekBar;
    private TextView durationTv;
    private ProgressBar loadingBar;
    private SimpleDateFormat dateFormat;
    private Date date;
    private RelativeLayout currentPosRl;
    private TextView currentPosTv;
    private String videoTitle;

    private TextView fullscreenTv;
    private ProgressBar progressBar;
    private OnShortVideoViewListener onShortVideoViewListener;

    public ShortVideoView(Context context) {
        super(context);
    }

    public ShortVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShortVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int setHalfLayout() {
        return R.layout.view_short_video;
    }

    @Override
    protected int setFullLayout() {
        return R.layout.view_fullscreen_video;
    }

    @Override
    public void init() {
        super.init();

        setOnTouchListener(this);

        // ****************************半屏****************************
        fullscreenTv = (TextView) findViewById(R.id.view_short_video_fullscreen_tv);
        ScreenAdapter.scaleView(fullscreenTv, ScreenAdapter.WRAP, ScreenAdapter.WRAP, 0, 0, 55, 50);
        ScreenAdapter.scaleTxtSize(fullscreenTv, 26);
        fullscreenTv.setCompoundDrawablePadding(ScreenAdapter.scaleX(10));

        progressBar = (ProgressBar) findViewById(R.id.view_short_video_progress_bar);
        ScreenAdapter.scaleView(progressBar, ScreenAdapter.MATCH, 6);

        // ****************************全屏****************************
        playStatusIv = (ImageView) findViewById(R.id.view_fullscreen_video_play_status_iv);
        ScreenAdapter.scaleView(playStatusIv, 188, 188, 0, 400);
        playStatusIv.setOnClickListener(this);

        playStatusTv = (TextView) findViewById(R.id.view_fullscreen_video_play_status_tv);
        ScreenAdapter.scaleView(playStatusTv, ScreenAdapter.WRAP, ScreenAdapter.WRAP, 0, 30);
        ScreenAdapter.scaleTxtSize(playStatusTv, 28);

        seekBar = (SeekBar) findViewById(R.id.view_fullscreen_video_seek_bar);
        ScreenAdapter.scaleView(seekBar, 1680, 56, 67, 68, 0, 48);
        seekBar.setPadding(0, ScreenAdapter.scaleY(20), 0, ScreenAdapter.scaleY(20));
        seekBar.setOnSeekBarChangeListener(this);

        durationTv = (TextView) findViewById(R.id.view_fullscreen_video_duration_tv);
        ScreenAdapter.scaleView(durationTv, ScreenAdapter.WRAP, ScreenAdapter.WRAP, 50, 0);
        ScreenAdapter.scaleTxtSize(durationTv, 30);

        currentPosRl = (RelativeLayout) findViewById(R.id.view_fullscreen_video_current_pos_rl);

        currentPosTv = (TextView) findViewById(R.id.view_fullscreen_video_current_pos_tv);
        currentPosTv.setPadding(ScreenAdapter.scaleX(5), 0, ScreenAdapter.scaleX(5), 0);
        ScreenAdapter.scaleTxtSize(currentPosTv, 28);

        loadingBar = (ProgressBar) findViewById(R.id.view_fullscreen_video_progress_bar);
        ScreenAdapter.scaleView(loadingBar, 150, 150);
    }

    @Override
    protected void changeUiToHalfScreenPlayClear() {

    }

    @Override
    protected void changeUiToFullscreenPlayClear() {
        playStatusIv.setVisibility(GONE);
        playStatusTv.setVisibility(GONE);
        seekBar.setVisibility(GONE);
        currentPosRl.setVisibility(GONE);
        durationTv.setVisibility(GONE);
        loadingBar.setVisibility(GONE);
    }

    @Override
    protected void changeUiToHalfScreenPlayShow() {

    }

    @Override
    protected void changeUiToFullscreenPlayShow() {
        playStatusIv.setImageResource(R.drawable.ic_pause);
        playStatusIv.setVisibility(VISIBLE);
        playStatusTv.setVisibility(VISIBLE);
        seekBar.setVisibility(VISIBLE);
        currentPosRl.setVisibility(VISIBLE);
        durationTv.setVisibility(VISIBLE);
        loadingBar.setVisibility(GONE);
    }

    @Override
    protected void changeUiToHalfScreenPrepare() {

    }

    @Override
    protected void changeUiToFullscreenPrepare() {
        playStatusIv.setVisibility(GONE);
        playStatusTv.setVisibility(GONE);
        seekBar.setVisibility(GONE);
        currentPosRl.setVisibility(GONE);
        durationTv.setVisibility(GONE);
        loadingBar.setVisibility(VISIBLE);
    }

    @Override
    protected void changeUiToHalfScreenSeekClear() {

    }

    @Override
    protected void changeUiToFullscreenSeekClear() {
        playStatusIv.setVisibility(GONE);
        playStatusTv.setVisibility(GONE);
        seekBar.setVisibility(GONE);
        currentPosRl.setVisibility(GONE);
        durationTv.setVisibility(GONE);
        loadingBar.setVisibility(VISIBLE);
    }

    @Override
    protected void changeUiToHalfScreenSeekShow() {

    }

    @Override
    protected void changeUiToFullscreenSeekShow() {
        playStatusIv.setVisibility(GONE);
        playStatusTv.setVisibility(GONE);
        seekBar.setVisibility(VISIBLE);
        currentPosRl.setVisibility(VISIBLE);
        durationTv.setVisibility(VISIBLE);
        loadingBar.setVisibility(VISIBLE);
    }

    @Override
    protected void changeUiToHalfScreenPause() {

    }

    @Override
    protected void changeUiToFullscreenPause() {
        playStatusIv.setImageResource(R.drawable.ic_play);
        playStatusIv.setVisibility(VISIBLE);
        playStatusTv.setText(videoTitle);
        playStatusTv.setVisibility(VISIBLE);
        seekBar.setVisibility(VISIBLE);
        currentPosRl.setVisibility(VISIBLE);
        durationTv.setVisibility(VISIBLE);
        loadingBar.setVisibility(GONE);
    }

    @Override
    protected void updateControllerUi(int progressIncrement) {
        if (isFullscreen()) {
            // TODO: 2017/6/9 gxd max和duration只用设置一次就可以了
            seekBar.setMax((int) hqPlayer.getDuration());
            seekBar.setProgress((int) hqPlayer.getCurrentPosition());
            seekBar.setKeyProgressIncrement(progressIncrement);

            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
            }
            if (date == null) {
                date = new Date();
            }
            date.setTime(hqPlayer.getDuration());
            durationTv.setText(dateFormat.format(date));
        } else {
            progressBar.setMax((int) hqPlayer.getDuration());
            progressBar.setProgress((int) hqPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onPlayerStatusChanged(@HqPlayer.HqPlayerStatus int playerStatus) {
        super.onPlayerStatusChanged(playerStatus);
        switch (playerStatus) {
            case HqPlayerStatus.PLAYER_STATE_PLAYING_CLEAR:
            case HqPlayerStatus.PLAYER_STATE_PLAYING_SHOW:
                if (onShortVideoViewListener != null) {
                    onShortVideoViewListener.onVideoPlaying();
                }
                break;
            case HqPlayerStatus.PLAYER_STATE_PAUSE:
                if (onShortVideoViewListener != null) {
                    onShortVideoViewListener.onVideoPaused();
                }
                break;
            case HqPlayerStatus.PLAYER_STATE_COMPLETION:
                if (onShortVideoViewListener != null) {
                    onShortVideoViewListener.onVideoCompleted();
                }
                break;
        }
    }

    @Override
    public void seekTo() {
        if (hqPlayer != null) {
            int progress = seekBar.getProgress();
            if (progress == 0) {// TODO: 2017/3/30 gxd 隐患代码
                hqPlayer.reset();
                try {
                    String videoUrl = hqPlayer.getDataSource();
                    hqPlayer.setDataSource(videoUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hqPlayer.setSurface(getSurface());
                hqPlayer.prepareAsync();
            } else if (progress == getDuration()) {
                progress -= 5000;
                hqPlayer.seekTo(progress);
            } else {
                hqPlayer.seekTo(progress);
            }
        }
    }

    /**
     * 快进
     */
    public void forward() {
        onKeyDown();

        if (hqPlayer.getHqPlayerStatus() == HqPlayerStatus.PLAYER_STATE_SEEK_SHOW) {
            changeUiToSeekShow();
        } else {
            playStatusIv.setImageResource(R.drawable.ic_forward);
            playStatusIv.setVisibility(VISIBLE);

            playStatusTv.setText("快进");
            playStatusTv.setVisibility(VISIBLE);

            currentPosRl.setVisibility(VISIBLE);
            durationTv.setVisibility(VISIBLE);
            loadingBar.setVisibility(GONE);
            seekBar.setVisibility(VISIBLE);
        }

        seekBar.setProgress(seekBar.getProgress() + 15000);
    }

    /**
     * 快退
     */
    public void backward() {
        onKeyDown();

        if (hqPlayer.getHqPlayerStatus() == HqPlayerStatus.PLAYER_STATE_SEEK_SHOW) {
            changeUiToSeekShow();
        } else {
            playStatusIv.setImageResource(R.drawable.ic_backward);
            playStatusIv.setVisibility(VISIBLE);

            playStatusTv.setText("快退");
            playStatusTv.setVisibility(VISIBLE);
            seekBar.setVisibility(VISIBLE);

            currentPosRl.setVisibility(VISIBLE);
            durationTv.setVisibility(VISIBLE);
            loadingBar.setVisibility(GONE);
        }
        seekBar.setProgress(seekBar.getProgress() - 15000);
    }

    public void setOnShortVideoViewListener(OnShortVideoViewListener onShortVideoViewListener) {
        this.onShortVideoViewListener = onShortVideoViewListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (onShortVideoViewListener != null) {
                    onShortVideoViewListener.onVideoTouchUp();
                }
                onScreenTouchUp();
                break;
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int max = seekBar.getMax();
        if (max == 0) {
            max = 1;
        }
        int marginLeft = (int) (1680L * progress / max);
        ScreenAdapter.scaleView(currentPosRl, 120, 68, 67 - 60 + marginLeft, 0, 0, 16 + 68);

        if (date == null) {
            date = new Date();
        }
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        date.setTime(progress);
        currentPosTv.setText(dateFormat.format(date));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        ViewParent vpdown = getParent();
        while (vpdown != null) {
            vpdown.requestDisallowInterceptTouchEvent(true);
            vpdown = vpdown.getParent();
        }
        if (handler != null) {
            handler.removeMessages(HANDLER_UPDATE_UI);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress == 0) {// TODO: 2017/3/30 gxd 隐患代码
            hqPlayer.reset();
            try {
                String videoUrl = hqPlayer.getDataSource();
                hqPlayer.setDataSource(videoUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            hqPlayer.setSurface(getSurface());
            hqPlayer.prepareAsync();
        } else if (progress == getDuration()) {
            progress -= 5000;
            hqPlayer.seekTo(progress);
        } else {
            hqPlayer.seekTo(progress);
        }
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_fullscreen_video_play_status_iv:
                if (isBasePlayerStatus(HqPlayerStatus.PLAYER_STATE_PLAYING)) {
                    pauseVideo();
                } else if (isBasePlayerStatus(HqPlayerStatus.PLAYER_STATE_PAUSE)) {
                    resumeVideo();
                }
                break;
        }
    }

    public interface OnShortVideoViewListener {
        void onVideoPlaying();

        void onVideoPaused();

        void onVideoCompleted();

        void onVideoTouchUp();
    }
}

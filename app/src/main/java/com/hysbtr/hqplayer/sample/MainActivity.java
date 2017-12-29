package com.hysbtr.hqplayer.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.hysbtr.hqplayer.HqMediaPlayerManager;
import com.hysbtr.hqplayer.constant.HqPlayerStatus;
import com.hysbtr.hqplayer.constant.HqPlayerType;
import com.hysbtr.hqplayer.constant.PlayerCodecType;
import com.hysbtr.hqplayer.sample.util.ScreenAdapter;
import com.hysbtr.hqplayer.sample.video.ShortVideoView;

public class MainActivity extends Activity implements ShortVideoView.OnShortVideoViewListener {
    private ShortVideoView videoView;
    private int i;
    private String[] videoUrls = {
            "http://p9-play-vp.autohome.com.cn/flvs/0A4648B7CEAD844D/2017-07-26/C45A84EC7291A85B-30.flv",
            "http://p9-play-vp.autohome.com.cn/flvs/E3BD4E39114FD258/2017-07-12/B3361CB67BAFFC5E-30.flv"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_short_video);

        videoView = findViewById(R.id.activity_short_video_view);
        ScreenAdapter.scaleView(videoView, 936, 528);
        videoView.setOnShortVideoViewListener(this);

        HqMediaPlayerManager.getInstance().setShowHqPlayerInfo(BuildConfig.DEBUG);
        HqMediaPlayerManager.getInstance().setPlayerType(HqPlayerType.IJK_PLAYER);
        HqMediaPlayerManager.getInstance().setPlayerCodecType(PlayerCodecType.CODEC_SOFT);
        videoView.attachPlayerAndPlay(videoUrls[0]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                i++;
                if (i > videoUrls.length - 1) {
                    i = 0;
                }

                HqMediaPlayerManager.getInstance().setPlayerCodecType(PlayerCodecType.CODEC_HARD);
                HqMediaPlayerManager.getInstance().setPlayerType(HqPlayerType.IJK_PLAYER);

                videoView.stopVideo();
                videoView.attachPlayerAndPlay(videoUrls[i]);
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                i--;
                if (i < 0) {
                    i = videoUrls.length - 1;
                }

                HqMediaPlayerManager.getInstance().setPlayerType(HqPlayerType.EXO_PLAYER);

                videoView.stopVideo();
                videoView.attachPlayerAndPlay(videoUrls[i]);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (videoView.isFullscreen()) {
                    if (videoView.isBasePlayerStatus(HqPlayerStatus.PLAYER_STATE_PLAYING)) {
                        videoView.pauseVideo();
                    } else if (videoView.isBasePlayerStatus(HqPlayerStatus.PLAYER_STATE_PAUSE)) {
                        videoView.resumeVideo();
                    }
                } else {
                    videoView.setFullscreen(true);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (videoView.isFullscreen()) {
                    videoView.forward();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (videoView.isFullscreen()) {
                    videoView.backward();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if (videoView.isFullscreen()) {
                    videoView.setFullscreen(false);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (videoView.isFullscreen()) {
                    videoView.seekTo();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView.isBasePlayerStatus(HqPlayerStatus.PLAYER_STATE_PAUSE)) {
            videoView.resumeVideo(videoUrls[i]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pauseVideo();
    }

    @Override
    protected void onDestroy() {
        videoView.stopVideo();
        super.onDestroy();
    }

    @Override
    public void onVideoPlaying() {

    }

    @Override
    public void onVideoPaused() {

    }

    @Override
    public void onVideoCompleted() {
        i++;
        if (i > videoUrls.length - 1) {
            i = 0;
        }
        videoView.stopVideo();
        videoView.attachPlayerAndPlay(videoUrls[i]);
    }

    @Override
    public void onVideoTouchUp() {
        videoView.setFullscreen(true);
    }
}

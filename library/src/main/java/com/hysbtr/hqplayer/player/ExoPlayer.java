package com.hysbtr.hqplayer.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hysbtr.hqplayer.HqMediaPlayerManager;
import com.hysbtr.hqplayer.core.IPlayer;
import com.hysbtr.hqplayer.core.IPlayerStatusListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by guoxiaodong on 2017/10/19 10:35
 * google的exo播放器的代理类
 */
public final class ExoPlayer extends Player.DefaultEventListener implements IPlayer {
    private static final String TAG = ExoPlayer.class.getSimpleName();
    private SimpleExoPlayer exoPlayer;
    private String videoUrl;
    private Context context;
    private IPlayerStatusListener.OnPreparedListener onPreparedListener;
    private IPlayerStatusListener.OnSeekToListener onSeekToListener;
    private IPlayerStatusListener.OnCompletionListener onCompletionListener;
    private boolean isBuffering;

    public ExoPlayer(Context context) {
        Log.i(TAG, "create ExoPlayer");
        this.context = context.getApplicationContext();
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        exoPlayer.addListener(this);
    }

    @Override
    public String getDataSource() {
        return videoUrl;
    }

    @Override
    public void setDataSource(String path) {
        videoUrl = path;
    }

    @Override
    public void prepareAsync() {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        DefaultHttpDataSourceFactory baseDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, bandwidthMeter, baseDataSourceFactory);
        Uri videoUri = Uri.parse(videoUrl);
        MediaSource mediaSource;

        int type = Util.inferContentType(videoUri);
        switch (type) {
            case C.TYPE_HLS:
                mediaSource = new HlsMediaSource(videoUri, dataSourceFactory, null, null);
                break;
            case C.TYPE_OTHER:
                mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
                break;
            case C.TYPE_SS:
                throw new IllegalStateException("Unsupported type: " + type);
            case C.TYPE_DASH:
                throw new IllegalStateException("Unsupported type: " + type);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }

        int loopCount = HqMediaPlayerManager.getInstance().getLoopCount();
        if (loopCount > 1) {
            mediaSource = new LoopingMediaSource(mediaSource, loopCount);
        }
        if (exoPlayer != null) {
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(this);
        }
    }

    @Override
    public void start() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void pause() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void stop() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
    }

    @Override
    public void seekTo(long posMsc) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(posMsc);
        }
    }

    @Override
    public void reset() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer.removeListener(this);
        }

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        exoPlayer.addListener(this);
    }

    @Override
    public void release() {
        if (exoPlayer != null) {
            exoPlayer.clearVideoSurface();
            exoPlayer.removeListener(this);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void setSurface(Surface surface) {
        if (exoPlayer != null) {
            exoPlayer.clearVideoSurface();
            exoPlayer.setVideoSurface(surface);
        }
    }

    @Override
    public boolean isPlaying() {
        if (exoPlayer == null) {
            return false;
        }
        int state = exoPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return exoPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (exoPlayer != null) {
            return exoPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        if (exoPlayer != null) {
            return exoPlayer.getDuration();
        }
        return 0;
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
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_IDLE:
                Log.i(TAG, "idle");
                break;
            case Player.STATE_READY:
                Log.i(TAG, "ready");
                if (isBuffering && onSeekToListener != null) {
                    onSeekToListener.onSeekTo(this, IMediaPlayer.MEDIA_INFO_BUFFERING_END);
                    isBuffering = false;
                }
                break;
            case Player.STATE_BUFFERING:
                Log.i(TAG, "buffering");
                if (onSeekToListener != null) {
                    onSeekToListener.onSeekTo(this, IMediaPlayer.MEDIA_INFO_BUFFERING_START);
                    isBuffering = true;
                }
                break;
            case Player.STATE_ENDED:
                Log.i(TAG, "end");
                if (onCompletionListener != null) {
                    onCompletionListener.onCompletion(this);
                }
                break;
        }
    }
}

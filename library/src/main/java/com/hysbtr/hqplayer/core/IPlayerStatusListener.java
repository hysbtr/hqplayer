package com.hysbtr.hqplayer.core;

/**
 * Created by guoxiaodong on 2017/10/19 20:41
 */
public interface IPlayerStatusListener {
    interface OnPreparedListener {
        void onPrepared(IPlayer player);
    }

    interface OnSeekToListener {
        void onSeekTo(IPlayer player, int what);
    }

    interface OnCompletionListener {
        void onCompletion(IPlayer player);
    }
}

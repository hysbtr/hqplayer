package com.hysbtr.hqplayer.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by guoxiaodong on 2017/10/10 09:43
 */
public abstract class HqVideoView extends BaseHqVideoView {
    private boolean isFullscreen;
    private View halfLayout;
    private View fullLayout;
    private int indexInOriginalParent;
    private ViewParent originalParent;
    private ViewGroup.LayoutParams params;
    private int halfWidth;
    private int halfHeight;
    private int halfLeftMargin;
    private int halfTopMargin;
    private int halfRightMargin;
    private int halfBottomMargin;

    public HqVideoView(Context context) {
        super(context);
    }

    public HqVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HqVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @android.support.annotation.LayoutRes
    protected abstract int setHalfLayout();

    @android.support.annotation.LayoutRes
    protected abstract int setFullLayout();

    @Override
    public void init() {
        super.init();
        int halfLayoutRes = setHalfLayout();
        if (halfLayoutRes != 0) {
            halfLayout = inflate(getContext(), halfLayoutRes, null);
            addView(halfLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        int fullLayoutRes = setFullLayout();
        if (fullLayoutRes != 0) {
            fullLayout = inflate(getContext(), fullLayoutRes, null);
            addView(fullLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (fullLayout != null) {
            fullLayout.setVisibility(GONE);
        }
    }

    public final boolean isFullscreen() {
        return isFullscreen;
    }

    public final void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;

        ViewGroup contentFrameLayout = (ViewGroup) getRootView().findViewById(android.R.id.content);
        View contentChildView = null;
        if (contentFrameLayout.getChildCount() > 0) {
            contentChildView = contentFrameLayout.getChildAt(0);
        }
        if (contentChildView != null && contentChildView instanceof ILayoutAttachDetach) {
            if (isFullscreen) {
                halfWidth = getMeasuredWidth();
                halfHeight = getMeasuredHeight();
                originalParent = getParent();
                indexInOriginalParent = ((ViewGroup) originalParent).indexOfChild(this);
                params = getLayoutParams();

                if (originalParent instanceof ILayoutAttachDetach) {
                    ((ILayoutAttachDetach) originalParent).detachViewFromParent(indexInOriginalParent);
                }

                if (params instanceof ViewGroup.MarginLayoutParams) {
                    halfLeftMargin = ((ViewGroup.MarginLayoutParams) params).leftMargin;
                    halfTopMargin = ((ViewGroup.MarginLayoutParams) params).topMargin;
                    halfRightMargin = ((ViewGroup.MarginLayoutParams) params).rightMargin;
                    halfBottomMargin = ((ViewGroup.MarginLayoutParams) params).bottomMargin;

                    ((ViewGroup.MarginLayoutParams) params).leftMargin = 0;
                    ((ViewGroup.MarginLayoutParams) params).topMargin = 0;
                    ((ViewGroup.MarginLayoutParams) params).rightMargin = 0;
                    ((ViewGroup.MarginLayoutParams) params).bottomMargin = 0;
                }

                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                ((ILayoutAttachDetach) contentChildView).attachViewToParent(this, ((ViewGroup) contentChildView).getChildCount(), params);
            } else {
                int contentChildViewIndex = ((ViewGroup) contentChildView).indexOfChild(this);
                ((ILayoutAttachDetach) contentChildView).detachViewFromParent(contentChildViewIndex);

                if (originalParent instanceof ILayoutAttachDetach) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) params).leftMargin = halfLeftMargin;
                        ((ViewGroup.MarginLayoutParams) params).topMargin = halfTopMargin;
                        ((ViewGroup.MarginLayoutParams) params).rightMargin = halfRightMargin;
                        ((ViewGroup.MarginLayoutParams) params).bottomMargin = halfBottomMargin;
                    }

                    params.width = halfWidth;
                    params.height = halfHeight;
                    ((ILayoutAttachDetach) originalParent).attachViewToParent(this, indexInOriginalParent, params);
                }
            }
            requestLayout();
            invalidate();
        }

        if (isFullscreen) {
            if (halfLayout != null) {
                halfLayout.setVisibility(GONE);
            }
            if (fullLayout != null) {
                fullLayout.setVisibility(VISIBLE);
            }
        } else {
            if (halfLayout != null) {
                halfLayout.setVisibility(VISIBLE);
            }
            if (fullLayout != null) {
                fullLayout.setVisibility(GONE);
            }
        }
        if (hqPlayer != null) {
            onPlayerStatusChanged(hqPlayer.getHqPlayerStatus());
        }
    }

    @Override
    protected final void changeUiToPlayClear() {
        if (isFullscreen) {
            changeUiToFullscreenPlayClear();
        } else {
            changeUiToHalfScreenPlayClear();
        }
    }

    protected abstract void changeUiToHalfScreenPlayClear();

    protected abstract void changeUiToFullscreenPlayClear();

    @Override
    protected final void changeUiToPlayShow() {
        if (isFullscreen) {
            changeUiToFullscreenPlayShow();
        } else {
            changeUiToHalfScreenPlayShow();
        }
    }

    protected abstract void changeUiToHalfScreenPlayShow();

    protected abstract void changeUiToFullscreenPlayShow();

    @Override
    protected final void changeUiToPrepare() {
        if (isFullscreen) {
            changeUiToFullscreenPrepare();
        } else {
            changeUiToHalfScreenPrepare();
        }
    }

    protected abstract void changeUiToHalfScreenPrepare();

    protected abstract void changeUiToFullscreenPrepare();

    @Override
    protected final void changeUiToSeekClear() {
        if (isFullscreen) {
            changeUiToFullscreenSeekClear();
        } else {
            changeUiToHalfScreenSeekClear();
        }
    }

    protected abstract void changeUiToHalfScreenSeekClear();

    protected abstract void changeUiToFullscreenSeekClear();

    @Override
    protected final void changeUiToSeekShow() {
        if (isFullscreen) {
            changeUiToFullscreenSeekShow();
        } else {
            changeUiToHalfScreenSeekShow();
        }
    }

    protected abstract void changeUiToHalfScreenSeekShow();

    protected abstract void changeUiToFullscreenSeekShow();

    @Override
    protected final void changeUiToPause() {
        if (isFullscreen) {
            changeUiToFullscreenPause();
        } else {
            changeUiToHalfScreenPause();
        }
    }

    protected abstract void changeUiToHalfScreenPause();

    protected abstract void changeUiToFullscreenPause();
}

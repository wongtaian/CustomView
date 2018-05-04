package com.konggeek.hz.customview.vg;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.konggeek.hz.customview.Util;


/**
 Created by wangtaian on 2018/4/29. */
public class ZoomViewPager extends ViewPager implements ViewPager.PageTransformer, ValueAnimator.AnimatorUpdateListener {
    private float touchX, touchY, translateX, translateY;
    /** STATE_IN 缩小状态，STATE_OUT 放大状态 */
    public static final int STATE_IN = 1, STATE_OUT = 2;
    private static final float MIN_SCALE = 0.9F;
    private int state = STATE_IN;
    private ViewGroup.LayoutParams params;
    private ValueAnimator va;
    private int width, height;
    private boolean isInit = true;

    private ZoomViewPagerStateListener l;
    private int margin;


    public ZoomViewPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ZoomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        margin = Util.toPx(80);
        va = new ValueAnimator();
        va.setDuration(500);
        va.setFloatValues(0, margin);
        va.addUpdateListener(this);
        setOffscreenPageLimit(2);
        setPageMargin(Util.toPx(20));
//        setPageTransformer(false, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        params = getLayoutParams();
        isInit = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, -1, oldh);
        if (isInit) {
            width = getWidth();
            height = getHeight();

        }
        if (state == STATE_IN) {
            //初始化时，就是缩小的状态
            params.width = width - margin * 2;
            params.height = height - margin * 2;
            setLayoutParams(params);
        }
        isInit = false;
    }


    @Override
    public void transformPage(@NonNull View page, float position) {
        if (state == STATE_IN) return;
        if (position < -1) {
            page.setScaleY(MIN_SCALE);
        } else if (position >= -1 && position <= 1) {
            float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
            page.setScaleY(scale);
            page.setScaleX(scale);

            if (position < 0) {
                page.setTranslationX(width * (1 - scale) / 2);
            } else {
                page.setTranslationX(-width * (1 - scale) / 2);
            }

        } else {
            page.setScaleY(MIN_SCALE);
        }

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int v = (int) ((float) animation.getAnimatedValue());
        if (state == STATE_OUT) {
            params.width = width - margin * 2 + v * 2;
            params.height = height - margin * 2 + v * 2;
        } else {
            params.width = width - v * 2;
            params.height = height - v * 2;
        }
        setLayoutParams(params);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int i = event.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
            translateX = 1;
            translateY = 1;
            return super.dispatchTouchEvent(event);
        } else if (i == MotionEvent.ACTION_MOVE) {
            translateX = touchX - event.getX();
            translateY = touchY - event.getY();
            return super.dispatchTouchEvent(event);
        } else if (i == MotionEvent.ACTION_UP) {
            if (Math.abs(touchX - event.getX()) <= 5 && Math.abs(touchY - event.getY()) <= 5) {
                onClickEvent();
                return true;
            }
            if (isIntercept()) {
                onMoveEvent();
                return true;
            }
            return super.dispatchTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean isIntercept() {
        return (state == STATE_IN && translateY >= 5 && Math.abs(translateX) <= 50) || (state == STATE_OUT && translateY <= -5 && Math.abs(translateX) <= 50);
    }

    private void onClickEvent() {
        state = state == STATE_OUT ? STATE_IN : STATE_OUT;
        changeSize();
//        va.start();
        if (l != null) l.onState(state);
    }

    private void onMoveEvent() {
        state = translateY >= 0 ? STATE_OUT : STATE_IN;
        changeSize();
//        va.start();
        if (l != null) l.onState(state);
    }

    private void changeSize() {
        if (state == STATE_OUT) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = width - margin * 2;
            params.height = height - margin * 2;
        }
        setLayoutParams(params);
    }

    public ZoomViewPager setZoomViewPagerStateListener(ZoomViewPagerStateListener l) {
        this.l = l;
        return this;
    }

    public interface ZoomViewPagerStateListener {
        void onState(int state);
    }
}


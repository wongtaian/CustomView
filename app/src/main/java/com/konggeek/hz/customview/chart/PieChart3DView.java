package com.konggeek.hz.customview.chart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 立体的饼图
 Created by wangtaian on 2018/3/26. */

public class PieChart3DView extends View {
    /** 中心点的位置 */
    private int centerX, centerY;
    private Paint paint;
    private Path path;
    private int ovalX = 450, ovalY = 180, thickness = 120;
    /** 饼图开始的角度，饼图滑过的角度 */
    private int startAngle = 90, sweepAngle = 215;
    private ValueAnimator valueAnimator;
    private float currentPercent = 0f;

    public PieChart3DView(Context context) {
        super(context);
    }

    public PieChart3DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.parseColor("#664567F3"));
        path = new Path();
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPercent = (float) animation.getAnimatedValue();
                sweepAngle = (int) (360 * currentPercent);
                invalidate();
            }
        });
        valueAnimator.setDuration(5000);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                startAngle = (int) (Math.random() * 360);
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    public PieChart3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PieChart3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // @author wangtaian  TIME: 2018/3/26  下午4:06  立体饼图，从下到上，从内到外的原则，依次画底部椭圆，背部立面，内部矩形，外部立面，顶部椭圆，顶部滑过的扇形。

        //底部半透明椭圆
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#664567F3"));
        canvas.drawOval(centerX - ovalX, centerY - ovalY, centerX + ovalX, centerY + ovalY, paint);

        // @author wangtaian  TIME: 2018/3/26  下午4:09  背部立面，饼图高度上依次画的弧线
        //从左侧边 180度到右侧边0度
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#99224A9F"));
        for (float i = 0; i < thickness; i += 0.3f) {
            path.arcTo(centerX - ovalX, centerY - ovalY - i, centerX + ovalX, centerY + ovalY - i, startAngle + sweepAngle, Math.abs(360 - startAngle - sweepAngle), true);
        }
        canvas.drawPath(path, paint);

        //内部矩形
        if (startAngle + sweepAngle > 270) {
            //根据扇形的滑过的角度，计算在顶部椭圆上的坐标
            int x = centerX + getCoordinate(startAngle + sweepAngle)[0];
            int y = centerY - thickness - getCoordinate(startAngle + sweepAngle)[1];
//            paint.setColor(Color.RED);
            paint.setColor(Color.parseColor("#B4EC51"));
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            path.reset();
            path.moveTo(x, y);
            path.lineTo(x, y + thickness);
            path.lineTo(centerX, centerY);
            path.lineTo(centerX, centerY - thickness);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (startAngle > 90) {
            //根据扇形的滑过的角度，计算在顶部椭圆上的坐标
            int x = centerX + getCoordinate(startAngle)[0];
            int y = centerY - thickness - getCoordinate(startAngle)[1];
//            paint.setColor(Color.RED);
            paint.setColor(Color.parseColor("#B4EC51"));
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            path.reset();
            path.moveTo(x, y);
            path.lineTo(x, y + thickness);
            path.lineTo(centerX, centerY);
            path.lineTo(centerX, centerY - thickness);
            path.close();
            canvas.drawPath(path, paint);
        }


        //从 startAngle 到 sweepAngle 的侧面
        path.reset();
        paint.reset();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#9DCC49"));
        for (float i = 0; i < thickness; i += 0.3f) {
            path.arcTo(centerX - ovalX, centerY - ovalY - i, centerX + ovalX, centerY + ovalY - i, startAngle, sweepAngle, true);
        }
        canvas.drawPath(path, paint);

        //从 0 度到 startAngle 的侧面
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#99224A9F"));
        for (float i = 0; i < thickness; i += 0.1f) {
            path.arcTo(centerX - ovalX, centerY - ovalY - i, centerX + ovalX, centerY + ovalY - i, startAngle + sweepAngle >= 360 ? startAngle + sweepAngle - 360 : 0, startAngle + sweepAngle > 360 ? 360 - sweepAngle : startAngle, true);
        }
        canvas.drawPath(path, paint);


        //顶部椭圆
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#334567F3"));
        canvas.drawOval(centerX - ovalX, centerY - ovalY - thickness, centerX + ovalX, centerY + ovalY - thickness, paint);


        //顶部扇形
        paint.setColor(Color.parseColor("#B4EC51"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(centerX - ovalX, centerY - ovalY - thickness,
                centerX + ovalX, centerY + ovalY - thickness, startAngle, sweepAngle, true, paint);

        // @author wangtaian  TIME: 2018/3/26  下午5:40  立面用 clipPath 方法更优雅一些，但计算太麻烦了。

       /* Path top = new Path();
        Path bottom = new Path();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        //填充弧形
        bottom.addArc(centerX - ovalX, centerY - ovalY, centerX + ovalX, centerY + ovalY, 0, 180);
        //两个矩形，底部椭圆到顶部椭圆的高度，从 sweep在椭圆上的点到start在椭圆上的点之间的长度
        bottom.lineTo(centerX + getCoordinate(180)[0], centerY + getCoordinate(180)[1] - thickness);
        bottom.lineTo(centerX + getCoordinate(0)[0], centerY + getCoordinate(0)[1] - thickness);
        bottom.close();
        //顶部弧形
        top.addOval(centerX - ovalX, centerY - ovalY - thickness, centerX + ovalX, centerY + ovalY - thickness, Path.Direction.CW);
        //切掉顶部弧形部分
        canvas.clipPath(top, Region.Op.DIFFERENCE);
        canvas.drawPath(bottom, paint);*/


    }

    /**
     获取坐标，相对{0,0}坐标
     @param angle
     @return
     */
    private int[] getCoordinate(@IntRange(from = 0, to = 359) int angle) {
        int x = (int) (ovalX * Math.sin(Math.PI * ((angle - 270f) / 180f)));
        int y = (int) (ovalY * Math.cos(Math.PI * ((angle - 270f) / 180f)));
        return new int[]{x, y};
    }

    /**
     设置饼图 的百分比，arcTo 的 sweepAngle 不支持360度
     @param sweepAngle
     @return
     */
    public PieChart3DView setSweepAngle(@IntRange(from = 0, to = 359) int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        valueAnimator.start();
        return super.onTouchEvent(event);
    }
}

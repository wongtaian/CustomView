package com.konggeek.hz.customview.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 仿时钟
 Created by wangtaian on 2018/3/28. */

public class ClockView extends View {
    private float centreX, centreY, radius;
    private Paint paint;
    private SimpleDateFormat simpleDateFormat;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        simpleDateFormat = new SimpleDateFormat("HH-mm-ss");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centreX = getWidth() / 2f;
        centreY = getHeight() / 2f;
        radius = (Math.min(getWidth(), getHeight()) / 2) - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        //画表盘
        canvas.drawCircle(centreX, centreY, radius, paint);
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 60; i++) {
            //画刻度
            canvas.drawCircle(centreX + radius - 30f, centreY, i % 5 == 0 ? 8f : 3f, paint);
            //每刻度之间弧度差6度，旋转画布6度。
            canvas.rotate(6, centreX, centreY);
        }
        canvas.restore();

        paint.setTextSize(36f);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.save();
        for (int i = 0; i < 12; i++) {
            drawText(canvas, i);
        }
        canvas.restore();


        String[] split = getTime().split("-");
        int second = Integer.parseInt(split[2]);
        int minute = Integer.parseInt(split[1]);
        int hour = Integer.parseInt(split[0]);
        //时针角度，12h=720m=43200s 当前的秒数 (hour*3600+minute*60+second) / 12*60*60 就是时针转过的百分比
        float hourAngle = ((hour >= 12 ? hour - 12 : hour) * 3600f + minute * 60f + second) / 120;
        //分针角度
        float minuteAngle = (minute * 60f + second) / 10f;
        //秒针角度
        int secondAngle = second * 6;
        paint.setColor(Color.WHITE);

        canvas.save();
        paint.setStrokeWidth(4);
        canvas.rotate(secondAngle, centreX, centreY);
        canvas.drawLine(centreX, centreY + 90, centreX, centreY - radius + 30, paint);
        canvas.restore();

        canvas.save();
        paint.setStrokeWidth(8);
        canvas.rotate(minuteAngle, centreX, centreY);
        canvas.drawLine(centreX, centreY + 60, centreX, centreY - radius + 120, paint);
        canvas.restore();

        canvas.save();
        paint.setStrokeWidth(16);
        canvas.rotate(hourAngle, centreX, centreY);
        canvas.drawLine(centreX, centreY + 30, centreX, centreY - radius + 180, paint);
        canvas.restore();

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        canvas.drawCircle(centreX, centreY, 6, paint);

        postInvalidateDelayed(1000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @SuppressLint("SimpleDateFormat")
    private String getTime() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /** 画表盘上的数字
     在中心点画文本，再根据旋转角度，挪动到指定位置，再旋转反向角度使文字正向；绘制文字后原路返回
     @param canvas
     @param i       */
    private void drawText(Canvas canvas, int i) {
        int degrees = i * 30;
        String s = String.valueOf(i == 0 ? 12 : i);
        Rect textBound = new Rect();
        //获取文字的占用范围
        paint.getTextBounds(s, 0, s.length(), textBound);


        canvas.rotate(degrees, centreX, centreY);
        canvas.translate(0,  -radius + 90f );
        canvas.rotate(-degrees, centreX, centreY);

        //字符绘制的原点是左下角，向左和下各挪动文字占用范围的一般，可以让字符的中心与刻度一致。
        canvas.drawText(s, centreX-(textBound.right-textBound.left)/2, centreY+(textBound.bottom-textBound.top)/2, paint);

        canvas.rotate(degrees, centreX, centreY);
        canvas.translate(0,  radius - 90f );
        canvas.rotate(-degrees, centreX, centreY);
    }


    /** 用三角函数计算表盘上的坐标，因为精度问题，计算的坐标并不精确。
     @param i
     @return
     */
    private float[] getCale(int i) {
        int angle = 60-(360 * i / 12);
        float x = (float) (centreX + (radius - 90f) * Math.cos(angle * Math.PI / 180f));
        float y = (float) (centreY - (radius - 90f) * Math.sin(angle * Math.PI / 180f));
        return new float[]{x-12f, y+12f};
    }
}

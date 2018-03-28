package com.konggeek.hz.customview.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        radius = (Math.min(getWidth(), getHeight()) / 2) - 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centreX, centreY, radius, paint);
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 60; i++) {
            canvas.drawCircle(centreX + radius - 30f, centreY, i % 5 == 0 ? 8f : 3f, paint);
            canvas.rotate(6, centreX, centreY);
        }
        canvas.restore();

        canvas.save();
        paint.setTextSize(32f);
        for (int i = 0; i < 12; i++) {
//            canvas.drawText(String.valueOf(i == 0 ? 12 : i), centreX - 16f, centreY - radius + 90, paint);
//            canvas.rotate(30, centreX, centreY);
            float[] cale = getCale(i);
            canvas.drawText(String.valueOf(i+1), cale[0], cale[1], paint);
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

    private float[] getCale(int i) {
        int angle = 60-(360 * i / 12);
        float x = (float) (centreX + (radius - 90f) * Math.cos(angle * Math.PI / 180f));
        float y = (float) (centreY - (radius - 90f) * Math.sin(angle * Math.PI / 180f));
        return new float[]{x-12f, y+12f};
    }

}

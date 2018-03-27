package com.konggeek.hz.customview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 柱状图
 Created by wong.taian on 2018/3/20. */

public class BarChartView extends View {
    private Paint paint;
    private int datas[] = {600, 400, 465, 330, 230};
    private int rect_width = 50; //  柱子的宽度
    private int rect_space = 50; //柱子之间的间隔
    private LinearGradient rectLinearGradient, textLinearGradient;
    private Paint rectPaint, textPaint;

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(4);  //
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        开始绘制

//        绘制x轴
        paint.setColor(Color.BLUE);
        canvas.drawLine(70, getHeight() - 30, getWidth() - 50, getHeight() - 30, paint);
//        绘制y轴
        canvas.drawLine(50, getHeight() - 50, 50, 30, paint);
//        绘制刻度
        paint.setColor(Color.parseColor("#7FA2F4"));
        paint.setTextSize(20f);
        for (int i = 1; i < 10; i++) {
            //Y轴值
            canvas.drawText(String.valueOf(i * 100), 10, (getHeight()) - i * ((getHeight()) / 10), paint);
        }
        dr(canvas);
    }

    private void dr(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            int bottom = getHeight() - 50;
            int top = getHeight() - 50 - datas[i];
            int left = 30 + rect_space * (i + 1) + rect_width * i;
            int right = left + rect_width;
            //立柱颜色线性渐变
            rectLinearGradient = new LinearGradient(left + 25, bottom, left + 25, bottom - (datas[i] / 2),
                    new int[]{Color.parseColor("#003C24BE"), Color.parseColor("#709AFF")}, null, Shader.TileMode.CLAMP);
            rectPaint.setShader(rectLinearGradient);
            canvas.drawRect(left, top, right, bottom, rectPaint);

            textLinearGradient = new LinearGradient(left + 25, top - 20, left + 25, top - 30,
                    new int[]{Color.parseColor("#98B8FF"), Color.WHITE}, new float[]{0.25f, 0.75f}, Shader.TileMode.CLAMP);
            textPaint.setShader(textLinearGradient);
            textPaint.setTextSize(20f);
            canvas.drawText("柱子" + i, left, top -10, textPaint);
        }

    }
}

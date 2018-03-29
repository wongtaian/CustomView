package com.konggeek.hz.customview.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 雷达图
 Created by wangtaian on 2018/3/28. */

public class RadarView extends View {
    private float centreX, centreY, radius, degrees;
    private Paint paint;
    private Path path;
    //一维：从内到外4个级别和外面文本，共5个
    //二维：7个类型
    //三维：点的坐标
    private float[][][] points = new float[5][7][2];
    private String[] type = {"击杀","生存","助攻","物理","魔法","防御","金钱"};
    private float[] data = new float[7];

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centreX = getWidth() / 2f;
        centreY = getHeight() / 2f;
        radius = Math.min(getWidth(), getHeight()) / 12f;
        degrees = 360f / 7f;

        for (int j = 4; j >= 0; j--) {
            for (int i = 0; i < 7; i++) {
                float[] cale = getCale(i, j);
                points[j][i][0] = cale[0];
                points[j][i][1] = cale[1];
            }
        }
        for (int i = 0; i < 7; i++) {
            data[i] = 4 * new Random().nextFloat()-1;
        }
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(24f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        for (int i = points.length - 2; i >= 0; i--) {
            float[][] point = points[i];
            path.rewind();
            paint.setColor(Color.argb( (i+1)*30,60, 84, 143));
            for (int j = 0; j < point.length; j++) {
                float[] floats = point[j];
                if (j==0) path.moveTo(floats[0],floats[1]);
                else path.lineTo(floats[0],floats[1]);
            }
            path.close();
            canvas.drawPath(path, paint);
        }

        paint.setColor(Color.WHITE);
        for (float[] floats : points[3]) {
            canvas.drawLine(centreX, centreY, floats[0], floats[1], paint);
        }
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(1);
        for (int i = 0; i < points[4].length; i++) {
            canvas.drawText(type[i], points[4][i][0], points[4][i][1], paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setColor(Color.RED);
        if (data != null && data.length == 7) {
            path.rewind();
            for (int i = 0; i < data.length; i++) {
                float[] cale = getCale(i, data[i]);
                if (i==0) path.moveTo(cale[0], cale[1]);
                else path.lineTo(cale[0],cale[1]);
            }
            path.close();
            canvas.drawPath(path, paint);
        }
        canvas.restore();
    }


    private float[] getCale(int i, float j) {
        float angle = 90f - (360f * i / 7f);
        float x = (float) (centreX + (radius * (j+1)) * Math.cos(angle * Math.PI / 180f));
        float y = (float) (centreY - (radius * (j+1)) * Math.sin(angle * Math.PI / 180f));
        return new float[]{x, y};
    }

    public void setData(float [] data){
        this.data = data;
        invalidate();
    }


}


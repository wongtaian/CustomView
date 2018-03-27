package com.konggeek.hz.customview.chart;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 贝塞尔曲线图表
 Created by wangtaian on 2018/3/27. */

public class BezierViw extends View {
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private ValueAnimator va;
    private float length;
    float[] pos = new float[2];
    /** 曲线数据 */
    private List<Float> dataList = new ArrayList<>();
    /** 数据的点 */
    private List<PointF> dataPoints = new ArrayList<>();
    /** 曲线上的所有点 */
    private List<PointF> curvePoints = new ArrayList<>();
    /** 标注时大小16 */
    private static final int MAX_POINT_RADIUS = 8;
    public static final int DAY_HOUR = 24;
    private int height = 400, width = 1080;


    public BezierViw(Context context) {
        super(context);
        init(context, null);
    }

    public BezierViw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BezierViw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BezierViw(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        path = new Path();

        va = new ValueAnimator();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pathMeasure.getPosTan((float) animation.getAnimatedValue(), pos, null);
                postInvalidate();
            }
        });
        va.setDuration(10000);

        pathMeasure = new PathMeasure();

        for (int j = 0; j <= 24; j++) {
            dataList.add((float) (Math.random()));
        }
        setDataList(dataList);
    }


    public void setDataList(List<Float> dataList) {
        if (dataList == null || dataList.isEmpty()) return;
        this.dataList = dataList;

        for (int i = 0; i < dataList.size(); i++) {
            Float y = dataList.get(i) * height;
            float x = width * i / DAY_HOUR;
            dataPoints.add(new PointF(x, y));
        }

        preparePoints(dataPoints);
        pathMeasure.setPath(path, false);
        //获取这条曲线的整体长度
        length = pathMeasure.getLength();

        va.setFloatValues(0, length);

        curvePoints.clear();
        /** 计算贝塞尔曲线上的坐标的方法一*/
        for (int i = 0; i < length; i++) {
            //PathMeasure 的 getPostTan 方法，三个参数，distance 长度，从path 的0点开始的一段距离，
            // pos 和 tan ，distance长度对应的点坐标和切线坐标，遍历 length 可以获取到整条曲线是的坐标点。
            pathMeasure.getPosTan(i, pos, null);
            curvePoints.add(new PointF(pos[0], pos[1]));
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        va.start();
        return super.onTouchEvent(event);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画数据点
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < dataPoints.size(); i++) {
            PointF pointF = dataPoints.get(i);
            paint.setColor(Color.RED);
            canvas.drawCircle(pointF.x, pointF.y, MAX_POINT_RADIUS, paint);
        }

        //画曲线
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawPath(path, paint);

        //按曲线路径移动点
        paint.setColor(Color.GREEN);
        if (length > 0) {
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(pos[0], pos[1], MAX_POINT_RADIUS*2, paint);
        }
    }


    /**
     用两个数据的前后数据点作为控制点，可以保证整体曲线的平滑
     @param pointFList
     */
    private void preparePoints(List<PointF> pointFList) {
        pointFList.add(0, new PointF(pointFList.get(0).x, pointFList.get(0).y));
        pointFList.add(new PointF(pointFList.get(pointFList.size() - 1).x, pointFList.get(pointFList.size() - 1).y));
        pointFList.add(new PointF(pointFList.get(pointFList.size() - 1).x, pointFList.get(pointFList.size() - 1).y));

        path.moveTo(pointFList.get(0).x, pointFList.get(0).y);
        List<PointF> pointCtrl = new ArrayList<>();
        pointCtrl.addAll(pointFList);
        for (int i = 1; i < pointFList.size() - 3; i++) {
            PointF ctrlPointA = new PointF();
            PointF ctrlPointB = new PointF();
            getCtrlPoint(pointFList, i, ctrlPointA, ctrlPointB);
            pointCtrl.add(ctrlPointA);
            pointCtrl.add(ctrlPointB);

            List<PointF> mControlPoints = new ArrayList<>();
            mControlPoints.add(pointFList.get(i));
            mControlPoints.add(ctrlPointA);
            mControlPoints.add(ctrlPointB);
            mControlPoints.add(pointFList.get(i + 1));


            /** 计算贝塞尔曲线上坐标的方法二，计算量比较大，不推荐
             参考于 https://github.com/venshine/BezierMaker
             */
            /*for (float t = 1; t < width; t++) {
                curvePoints.add(new PointF(deCasteljauX(3, 0, t / width, mControlPoints),
                        deCasteljauY(3, 0, t / width, mControlPoints)));
            }*/
            path.cubicTo(ctrlPointA.x, ctrlPointA.y, ctrlPointB.x, ctrlPointB.y, pointFList.get(i + 1).x, pointFList.get(i + 1).y);
        }
    }

    private static final float CTRL_VALUE_A = 0.15f;
    private static final float CTRL_VALUE_B = 0.15f;

    private void getCtrlPoint(List<PointF> pointFList, int currentIndex, PointF ctrlPointA, PointF ctrlPointB) {
        ctrlPointA.x = pointFList.get(currentIndex).x + (pointFList.get(currentIndex + 1).x - pointFList.get(currentIndex - 1).x) * CTRL_VALUE_A;
        ctrlPointA.y = pointFList.get(currentIndex).y + (pointFList.get(currentIndex + 1).y - pointFList.get(currentIndex - 1).y) * CTRL_VALUE_A;
        ctrlPointB.x = pointFList.get(currentIndex + 1).x - (pointFList.get(currentIndex + 2).x - pointFList.get(currentIndex).x) * CTRL_VALUE_B;
        ctrlPointB.y = pointFList.get(currentIndex + 1).y - (pointFList.get(currentIndex + 2).y - pointFList.get(currentIndex).y) * CTRL_VALUE_B;
    }

    /**
     deCasteljau算法
     @param i 阶数
     @param j 点
     @param t 时间
     @return
     */
    private float deCasteljauX(int i, int j, float t, List<PointF> mControlPoints) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).x + t * mControlPoints.get(j + 1).x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t, mControlPoints) + t * deCasteljauX(i - 1, j + 1, t, mControlPoints);
    }

    /**
     deCasteljau算法
     @param i 阶数
     @param j 点
     @param t 时间
     @return
     */
    private float deCasteljauY(int i, int j, float t, List<PointF> mControlPoints) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).y + t * mControlPoints.get(j + 1).y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t, mControlPoints) + t * deCasteljauY(i - 1, j + 1, t, mControlPoints);
    }

}

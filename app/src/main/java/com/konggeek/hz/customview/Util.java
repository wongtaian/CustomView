package com.konggeek.hz.customview;

/**
 Created by wangtaian on 2018/5/3. */
public class Util {
    /**
     根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int toPx(float dpValue) {
        float scale = APP.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

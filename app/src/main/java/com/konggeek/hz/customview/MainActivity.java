package com.konggeek.hz.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.konggeek.hz.customview.vg.ZoomViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String FILEPATH = Environment.getExternalStorageDirectory() + "/customview";
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ZoomViewPager viewPager = findViewById(R.id.zoom_view_pager);
        for (int i = 0; i < 30; i++) {
            data.add(String.valueOf(i));
        }
        viewPager.setAdapter(mAdapter);



        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewSaveToImage(viewPager);
            }
        });
    }

    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(R.mipmap.timg);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };


    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);
        // 添加水印
//        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp, "@ KongGeek"));
        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
//                File sdRoot = Environment.getExternalStorageDirectory();
                String FILEPATH = Environment.getExternalStorageDirectory() + "/customview";
                File fileParent = new File(FILEPATH);
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }

                File file = new File(FILEPATH, System.currentTimeMillis() + ".PNG");
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        // 水印的颜色
        p.setColor(Color.RED);
        // 水印的字体大小
        p.setTextSize(16);
        p.setAntiAlias(true);// 去锯齿
        canvas.drawBitmap(target, 0, 0, p);
        // 在中间位置开始添加水印
        canvas.drawText(str, w / 2, h / 2, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }
}

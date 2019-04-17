package com.example.zhoumohan.luckymorning.common.widget.mapview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class ProvinceItem {
    private Path path;
    /**
     * 绘制颜色
     */
    private int drawColor;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            paint.clearShadowLayer();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.YELLOW);
            canvas.drawPath(path, paint);
            //选中时，绘制描边效果
            paint.setStrokeWidth(1f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            canvas.drawPath(path, paint);
        } else {
            //选中时，绘制描边效果
            paint.setStrokeWidth(2f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setShadowLayer(8,0,0,0xffffff);
            paint.setColor(Color.BLACK);
            canvas.drawPath(path, paint);

            paint.clearShadowLayer();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);
        }
    }

    public boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        //取path有rect的交集区域
        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region.contains((int)x,(int)y);
    }
}

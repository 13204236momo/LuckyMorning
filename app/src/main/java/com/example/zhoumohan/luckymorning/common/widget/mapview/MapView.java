package com.example.zhoumohan.luckymorning.common.widget.mapview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.zhoumohan.luckymorning.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MapView extends View {

    private Context context;
    private Paint paint;
    private RectF totalRect;
    private float scale = 1f;
    private ProvinceItem select;
    private List<ProvinceItem> list = new ArrayList<>();
    private int[] colors = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFF42BA67};

    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element rootElement = document.getDocumentElement();
                NodeList nodeList = rootElement.getElementsByTagName("path");
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;
                List<ProvinceItem> itemList = new ArrayList<>();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    //将路径信息转化为path对象
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem item = new ProvinceItem(path);
                    item.setDrawColor(setColor(i));
                    RectF rectF = new RectF();
                    path.computeBounds(rectF, true);
                    left = left == -1 ? rectF.left : Math.min(left, rectF.left);
                    top = top == -1 ? rectF.top : Math.min(top, rectF.top);
                    right = right == -1 ? rectF.right : Math.max(right, rectF.right);
                    bottom = bottom == -1 ? rectF.bottom : Math.max(bottom, rectF.bottom);
                    itemList.add(item);
                }
                list = itemList;
                totalRect = new RectF(left, top, right, bottom);
                //刷新界面
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        invalidate();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        loadThread.start();
    }

    /**
     * 设置颜色
     *
     * @param i
     * @return
     */
    private int setColor(int i) {
        return colors[i % colors.length];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (totalRect != null) {
            double mapWidth = totalRect.width();
            scale = (float) (width / mapWidth);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event.getX() / scale, event.getY() / scale);
        return super.onTouchEvent(event);
    }

    private void handleTouch(float x, float y) {
        if (list == null) {
            return;
        }
        for (ProvinceItem item : list) {
            if (item.isTouch(x, y)) {
                select = item;
                postInvalidate();
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list != null) {
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem item : list) {
                if (item != select) {
                    item.drawItem(canvas, paint, false);
                } else {
                    item.drawItem(canvas, paint, true);
                }
            }
        }

        /**
         * @param bitmap 位图图片
         * @param tileX x方向渲染模式
         * @param tileY y方向渲染模式
         */
//        Shader mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        Paint mPaint = new Paint();
//        mPaint.setShader(mShader);
//        canvas.drawCircle(250, 250, 250, mPaint);
    }
}

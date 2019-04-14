package com.example.zhoumohan.luckymorning.common.widget.parallaxView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhoumohan.luckymorning.R;

import java.lang.reflect.Constructor;

public class ParallaxLayoutInflater extends LayoutInflater {

    private static final String TAG = "ParallaxLayoutInflater";
    private ParallaxFragment fragment;

    protected ParallaxLayoutInflater(Context context) {
        super(context);
    }

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext, ParallaxFragment fragment) {
        super(original, newContext);
        this.fragment = fragment;
        setFactory2(new ParallaxFactory());
    }

    @Override
    public LayoutInflater cloneInContext(Context context) {
        return new ParallaxLayoutInflater(this, context, fragment);
    }


    class ParallaxFactory implements Factory2 {

        private final String[] sClassPrefix = {"android.widget.", "android.view."};
        private int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out
        };

        @Override
        public View onCreateView(View view, String name, Context context, AttributeSet attributeSet) {
            Log.d(TAG, "name=" + name);
            View view1 = null;
            //将系统控件加上包名
            if (name.contains(".")) { //自定义控件是全路径
                view1 = createMyView(name, context, attributeSet);

            } else {
                for (String prefix : sClassPrefix) {
                    view1 = createMyView(prefix + name, context, attributeSet);
                    if (view1!= null) {
                        break;
                    }
                }
            }

            //将AttributeSet取出自定义属性，封装成javaBean，设置到viewTag
            TypedArray a = context.obtainStyledAttributes(attributeSet, attrIds);
            if (a != null && a.length() > 0) {
                ParallaxViewTag tag = new ParallaxViewTag();
                tag.alphaIn = a.getFloat(0, 0f);
                tag.alphaOut = a.getFloat(1, 0f);
                tag.xIn = a.getFloat(2, 0f);
                tag.xOut = a.getFloat(3, 0f);
                tag.yIn = a.getFloat(4, 0f);
                tag.yOut = a.getFloat(5, 0f);

                view1.setTag(R.id.parallax_view_tag, tag);

            }
            a.recycle();
            fragment.getParallaxViews().add(view1);
            return view1;
        }

        private View createMyView(String s, Context context, AttributeSet attributeSet) {
            //通过反射创建控件对象
            try {
                Class clazz = Class.forName(s);
                Constructor<View> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
                return constructor.newInstance(context, attributeSet);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        public View onCreateView(String s, Context context, AttributeSet attributeSet) {
            return null;
        }
    }
}

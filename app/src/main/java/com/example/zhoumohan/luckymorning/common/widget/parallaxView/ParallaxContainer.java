package com.example.zhoumohan.luckymorning.common.widget.parallaxView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.start.Start1Activity;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private ParallaxPagerAdapter adapter;
    private List<ParallaxFragment> fragments;
    private ImageView ivPerson;

    public void setIvPerson(ImageView ivPerson) {
        this.ivPerson = ivPerson;
    }

    public ParallaxContainer(@NonNull Context context) {
        super(context);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUp(int... childIds) {

        fragments = new ArrayList<>();
        for (int i = 0; i < childIds.length; i++) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle args = new Bundle();
            args.putInt("layoutId", childIds[i]);
            fragment.setArguments(args);
            fragments.add(fragment);
        }

        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setId(R.id.parallax_pager);
        addView(viewPager);
        Start1Activity activity = (Start1Activity) getContext();
        adapter = new ParallaxPagerAdapter(activity.getSupportFragmentManager(), fragments);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(this);
    }


    //根据用户滑动的距离 去显示动画
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int containerWidth = getWidth();
        ParallaxFragment inFragment = null;
        ParallaxFragment outFragment = null;
        try {
            outFragment = fragments.get(position - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            inFragment = fragments.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outFragment != null) {
            List<View> outViews = outFragment.getParallaxViews();
            for (View view : outViews) {
                ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                if (tag == null) {
                    continue;
                }
                ViewHelper.setTranslationX(view, containerWidth - positionOffsetPixels * tag.xOut);
                ViewHelper.setTranslationY(view, containerWidth - positionOffsetPixels * tag.yOut);

            }
        }

        if (inFragment != null) {
            List<View> inViews = inFragment.getParallaxViews();
            for (View view : inViews) {
                ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                if (tag == null) {
                    continue;
                }
                ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xIn);
                ViewHelper.setTranslationY(view, 0 - positionOffsetPixels * tag.yIn);

            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            ivPerson.setVisibility(GONE);
        } else {
            ivPerson.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animationDrawable = (AnimationDrawable) ivPerson.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                animationDrawable.start();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                animationDrawable.stop();
                break;
        }
    }
}

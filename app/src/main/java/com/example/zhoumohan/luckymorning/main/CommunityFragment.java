package com.example.zhoumohan.luckymorning.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseFragment;
import com.example.zhoumohan.luckymorning.common.entity.TabEntity;
import com.example.zhoumohan.luckymorning.common.widget.Fab;
import com.example.zhoumohan.luckymorning.common.widget.FontIconView;
import com.example.zhoumohan.luckymorning.community.AddRecordActivity;
import com.example.zhoumohan.luckymorning.community.CommunityDetailMvpFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityFragment extends BaseFragment {

    @BindView(R.id.tl_community)
    CommonTabLayout tlCommunity;
    @BindView(R.id.vp_community)
    ViewPager vpCommunity;

    @BindView(R.id.font_release)
    FontIconView fontRelease;

    private String[] mTitles = {"动态", "任务", "关注"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private CommunityDetailMvpFragment communityDetailMvpFragment;
    private NewsFragment3 newsFragment3;
    private NewsFragment4 newsFragment4;
    private ArrayList<Fragment> mFragments = new ArrayList<>();



    @Override
    protected int getLayoutResID() {
        return R.layout.community_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        communityDetailMvpFragment = new CommunityDetailMvpFragment();
        newsFragment3 = new NewsFragment3();
        newsFragment4 = new NewsFragment4();
        mFragments.add(communityDetailMvpFragment);
        mFragments.add(newsFragment3);
        mFragments.add(newsFragment4);

        vpCommunity.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        vpCommunity.setCurrentItem(0);

        if (mTabEntities.size() == 0) {
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], R.drawable.ic_launcher_background, R.drawable.ic_launcher_background));
            }
        }

        tlCommunity.setTabData(mTabEntities);

        initEvent();

    }

    @OnClick({R.id.font_release})
    void onClick(View view){
        switch (view.getId()){
            case R.id.font_release:
                startActivity(new Intent(getActivity(), AddRecordActivity.class));
                break;

        }
    }

    private void initEvent(){
        vpCommunity.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tlCommunity.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tlCommunity.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpCommunity.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }





    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}
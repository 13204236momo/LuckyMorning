package com.example.zhoumohan.luckymorning.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.community.CommunityDetailMvpFragment;
import com.example.zhoumohan.luckymorning.main.adapter.MainPagerAdapter;
import com.example.zhoumohan.luckymorning.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;

    private CommunityFragment newsFragment;
    private NewsFragment1 newsFragment1;
    private CommunityDetailMvpFragment newsFragment2;
    private NewsFragment3 newsFragment3;
    private NewsFragment4 newsFragment4;

    private String[] colors = new String[5];
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_main);

        initFragment();
        initUI();
    }

    private void initFragment(){
        newsFragment = new CommunityFragment();
        newsFragment1 = new NewsFragment1();
        newsFragment2 = new CommunityDetailMvpFragment();
        newsFragment3 = new NewsFragment3();
        newsFragment4 = new NewsFragment4();
        mFragmentList.add(newsFragment);
        mFragmentList.add(newsFragment1);
        mFragmentList.add(newsFragment2);
        mFragmentList.add(newsFragment3);
        mFragmentList.add(newsFragment4);
    }

    private void initUI() {
        StatusBarUtils.setTranslucentForImageViewInFragment(this, 0);
        viewPager = findBy(R.id.vp_horizontal_ntb);
        navigationTabBar = findBy(R.id.ntb_horizontal);

        colors = getResources().getStringArray(R.array.default_preview);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),mFragmentList));

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Heart")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Cup")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Diploma")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Flag")
                        .badgeTitle("icon")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Medal")
                        .badgeTitle("777")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }



}

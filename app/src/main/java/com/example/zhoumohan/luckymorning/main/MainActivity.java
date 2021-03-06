package com.example.zhoumohan.luckymorning.main;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.zhoumohan.common_library.globalNetWorkChange.NetType;
import com.zhoumohan.common_library.globalNetWorkChange.Network;
import com.zhoumohan.common_library.globalNetWorkChange.NetworkManager;
import com.zhoumohan.common_library.globalNetWorkChange.utils.Constants;
import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseActivity;
import com.example.zhoumohan.luckymorning.community.CommunityDetailMvpFragment;
import com.example.zhoumohan.luckymorning.main.adapter.MainPagerAdapter;
import com.example.zhoumohan.luckymorning.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends BaseActivity {

    static {
        System.loadLibrary("native-lib");
    }

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

        //注册
        NetworkManager.getDefault().register(this);

        initFragment();
        initUI();
        upDateApk();
        getMessage();
    }

    private void getMessage() {
        PackageManager manager = getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
    }


    private void initFragment() {
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
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mFragmentList));

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

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG, "WIFI");
                break;
            case CMNET:
            case CMWAP:
                //有网络
                Log.e(Constants.LOG_TAG, "移动数据连接");
                break;
            case NONE:
                //没有网络
                Log.e(Constants.LOG_TAG, "网络连接失败");
                break;
        }
    }

    private void upDateApk() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkCallingPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册，解绑
        NetworkManager.getDefault().unRegister(this);
        NetworkManager.getDefault().unRegisterAll();

    }


    /**
     * 合成安装包
     *
     * @param oldApk
     * @param newApk
     * @param outputPath 合成安装包的路径
     */
    public native void bsPath(String oldApk, String newApk, String outputPath);
}

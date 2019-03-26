package com.example.zhoumohan.luckymorning.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseFragment;
import com.example.zhoumohan.luckymorning.camera.CaptureActivity;
import com.example.zhoumohan.luckymorning.util.CameraUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class NewsFragment4 extends BaseFragment {

    @BindView(R.id.iv_qr)
    ImageView ivQr;

    private static final int REQUEST_SCAN = 0;
    @Override
    protected int getLayoutResID() {
        return R.layout.item_vp4;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
    }


    @OnClick(R.id.iv_qr)
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_qr:
                getRuntimePermission();
                break;
        }

    }

    // 获得运行时权限
    private void getRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (getActivity().checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                    getActivity().checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            } else {
                jumpScanPage();
            }
        }
    }

    // 跳转到扫码页
    private void jumpScanPage() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), data.getStringExtra(CameraUtil.BAR_CODE), Toast.LENGTH_LONG).show();
        }
    }


}

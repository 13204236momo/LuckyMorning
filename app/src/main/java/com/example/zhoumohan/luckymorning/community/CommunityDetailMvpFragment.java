package com.example.zhoumohan.luckymorning.community;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.common_library.utils.UriParseUtils;
import com.example.zhoumohan.luckymorning.R;
import com.example.zhoumohan.luckymorning.base.BaseMvpFragment;
import com.example.zhoumohan.luckymorning.main.MainActivity;


import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityDetailMvpFragment extends BaseMvpFragment<CommunityPresenter, CommunityContract.Model> implements CommunityContract.View {

    @BindView(R.id.btn_apk)
    Button button;

    @Override
    protected int getLayoutResID() {
        return R.layout.community_detail_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);


    }

    @Override
    public void upDate() {

    }

    @OnClick(R.id.btn_apk)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apk:
                lode();
                break;
        }

    }

    private void lode() {
        //从服务器下载patch（增量apk）
        new AsyncTask<Void, Void, File>() {

            @Override
            protected File doInBackground(Void... voids) {
                String oldApk = getActivity().getApplicationInfo().sourceDir;
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                String output = CreateNewApk().getAbsolutePath();
                ((MainActivity) getActivity()).bsPath(oldApk, patch, output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                UriParseUtils.installApk(getContext(), file);
            }
        }.execute();
    }

    private File CreateNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(),"bsdoff.apk");
        if (!newApk.exists()){
            try {
                newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newApk;
    }


}

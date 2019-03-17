package com.example.common_library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import java.io.File;

public class UriParseUtils {
    /**
     * 创建一个文件输出路径的Uri
     * @param context
     * @param file
     * @return转换后的Scheme 为FileProvider的Uri
     */
    private static Uri getUriForFile(Context context, File file){
        return FileProvider.getUriForFile(context,getFileProvider(context),file);
    }

    /**
     * 获取Fileprovider路径，适配6.0+
     * 获取Fileprovider路径
     * @param context
     * @return
     */
    private static String getFileProvider(Context context) {
        return context.getApplicationInfo().packageName + ".fileprovider";
    }

    /**
     * 安装APK
     * @param context
     * @param apkFile
     */
    public static void installApk(Context context,File apkFile){
        if (!apkFile.exists()){
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Uri fileUri = getUriForFile(context,apkFile);
            intent.setDataAndType(fileUri,"application/vnd.android.package-archive");
        }else {
            intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}

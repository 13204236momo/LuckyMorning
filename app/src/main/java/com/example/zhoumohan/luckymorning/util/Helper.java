package com.example.zhoumohan.luckymorning.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    public static Context mContext;
    private static Toast mToast;
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 全局Toast
     *
     * @param str
     */
    public static void showToast(String str) {
        if (!isMainThread(Thread.currentThread().getName())) {
          Log.e("error","不能在异步线程调用showToast");
            return;
        }
        if (mContext == null) return;
        if (TextUtils.isEmpty(str)) return;
        if (mToast == null) {
            mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    /**
     * 是否在主线程
     *
     * @param aThreadName
     * @return
     */
    public static boolean isMainThread(String aThreadName) {
        return aThreadName.equals("main");
    }

    /**
     * 获取状态栏高度
     * */
    public static int getStatusBarHeight(Context context) {
        try {
            @SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dip2px(float dipValue) {
        if (mContext == null) return 0;
        return dip2px(mContext, dipValue);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        if (mContext == null) return 0;
        return px2dip(mContext, pxValue);
    }

    //中粮生成token
    public static String createToken(HashMap<String, String> map){

        String parameter_str = "";
        /*将参数的key和value拼接成字符串*/
        for(Map.Entry<String, String> entry:map.entrySet()){

            parameter_str+=entry.getKey();
            parameter_str+=(String)entry.getValue();
        }

        /*对拼接后的字符串进行排序*/
        parameter_str = sortByChar(parameter_str);

        /*对排序后的字符串转大写*/
        parameter_str=parameter_str.toUpperCase();

        /*md5计算*/
        return encryptMD5ToString(parameter_str);

    }

    /*对字符串进行ASCII码升序排列*/
    private static String sortByChar(String str){
        char[] cArray = str.toCharArray();
        for(int i=0;i<cArray.length-1;i++){
            for(int k=0; k<cArray.length-1-i;k++){
                if((int)cArray[k] > (int)cArray[k+1]){
                    char temp = cArray[k];
                    cArray[k] = cArray[k+             1];
                    cArray[k+1] = temp;
                }
            }
        }
        return String.valueOf(cArray);
    }


    /**
     * 获取网络的时时网速，使用方法是：
     * 每隔一段时间读取一次总流量，然后用本次和前一次的差除以间隔时间来获取平均速度，再换算为 K/s M/s
     * 等单位，显示即可。
     *
     * @return 实时的网速（单位byte）
     */
    public static int getNetSpeedBytes() {
        String line;
        String[] segs;
        int received = 0;
        int i;
        int tmp = 0;
        boolean isNum;
        try {
            FileReader fr = new FileReader("/proc/net/dev");
            BufferedReader in = new BufferedReader(fr, 500);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan")) {
                    segs = line.split(":")[1].split(" ");
                    for (i = 0; i < segs.length; i++) {
                        isNum = true;
                        try {
                            tmp = Integer.parseInt(segs[i]);
                        } catch (Exception e) {
                            isNum = false;
                        }
                        if (isNum == true) {
                            received = received + tmp;
                            break;
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            return -1;
        }
        return received;
    }

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public static String encryptMD5ToString(String data) {
        return encryptMD5ToString(data.getBytes()).toLowerCase();
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */
    public static String encryptMD5ToString(byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptMD5(byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * hash加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

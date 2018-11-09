package com.example.zhoumohan.luckymorning.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * Created by jiangsh on 2017/3/21.
 */

public class DESUtil {
    // 向量
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";
    // 加解密key
    public final static String KEY = "71fe6c299e7ec3e29ab0bb82b4cdb77a";

    /**
     * String plainText 需加密内容, String secretKey 密匙;
     * @return String 加密后内容
     * @Description 加密方法
     * @date 2014-10-14 16:12:04
     * @author 龙哲
     */
    public static String encode(String plainText, String secretKey) throws Exception {
        // {'data': {},'page': {},'code': 'FAIL','errorMsg': '操作失败'}
        String result = "";
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        result = Base64Utils.encode(encryptData);
        return result;
    }

    /**
     *  参数列表 String encryptText 需解密内容, String secretkey 密匙;
     * @return String 已解密内容
     * @Description 解密方法
     * @date 2014-10-14 16:12:46
     * @author 龙哲
     */
    public static String decode(String encryptText, String secretKey) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64Utils.decode(encryptText));
        return new String(decryptData, encoding);
    }


    /**
     * 需要加密的接口
     */
  //  public static String[] urls=new String[]{API_ORDERRESERVATION,API_ORDERTAKEOUT,API_WALLET,API_ADDGOODSORDER,API_ORDERWALLET,API_ADDGROUPGOODSORDER,API_QRCODEWALLET,API_PROPERTYWALLET};
    public static String[] urls=new String[0];
    /**
     * 判断是否是这个接口
     * @param url
     * @return
     */
    public static boolean isContent(String url){
        for (int i = 0; i < urls.length; i++) {
            if( urls[i].equals(url)){
                return true;
            }
        }
        return false;
    }
}

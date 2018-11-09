
package com.example.zhoumohan.luckymorning.common.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.zhoumohan.luckymorning.common.http.result.BaseResult;
import com.example.zhoumohan.luckymorning.common.log.Lg;
import com.example.zhoumohan.luckymorning.util.DESUtil;
import com.example.zhoumohan.luckymorning.util.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class OkHttpHelp {
    private static OkHttpHelp mOkHttpHelperInstance;
    private static OkHttpClient mClientInstance;

    private Handler mDelivery;

    /**
     * 单例模式，私有构造函数，构造函数里面进行一些初始化
     */
    private OkHttpHelp() {
        mClientInstance = new OkHttpClient();
        mClientInstance.setConnectTimeout(30, TimeUnit.SECONDS);
        mClientInstance.setReadTimeout(30, TimeUnit.SECONDS);
        mClientInstance.setWriteTimeout(30, TimeUnit.SECONDS);

        mDelivery = new Handler(Looper.getMainLooper());
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        mClientInstance.setSslSocketFactory(getSslInfo().getSocketFactory());
        mClientInstance.setHostnameVerifier(DO_NOT_VERIFY);
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkHttpHelp getinstance() {
        if (mOkHttpHelperInstance == null) {
            synchronized (OkHttpHelp.class) {
                if (mOkHttpHelperInstance == null) {
                    mOkHttpHelperInstance = new OkHttpHelp();
                }
            }
        }
        return mOkHttpHelperInstance;
    }


    /**
     * get请求
     *
     * @param context
     * @param url
     * @param params
     * @param callback
     */
    public void httpGet(Context context, String url, HashMap<String, String> params, ServiceCallback callback) {
        String logStr = new String(url);
        String urlStr = new String(url);
        urlStr += "?";
        logStr += "?";
        try {
            if (params != null) {
                Iterator<Entry<String, String>> it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> entry = it.next();
                    if (entry.getValue() != null) {
//                        urlStr+=entry.getKey()+"="+ DESUtil.encode(URLEncoder.encode(entry.getValue(), "utf-8"),DESUtil.KEY)+"&";

                        if(DESUtil.isContent(url)){
                            logStr+=entry.getKey()+"="+DESUtil.encode(entry.getValue(),DESUtil.KEY)+"&";
                            urlStr+=entry.getKey()+"="+ DESUtil.encode(URLEncoder.encode(entry.getValue(), "utf-8"),DESUtil.KEY)+"&";
                        }else{
                            logStr += entry.getKey() + "=" + entry.getValue() + "&";
                            urlStr += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8") + "&";
                        }

                    }
                }
                params = null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		Lg.i("url:", "blue:"+logStr);


        final Request request = new Request.Builder().url(urlStr).build();
        Call call = mClientInstance.newCall(request);
        call.enqueue(okCallBack(callback, logStr));
    }


    /**
     * post  请求
     *
     * @param context
     * @param url
     * @param map
     * @param fileName
     * @param files
     * @param callback
     */
    public void httpPost(Context context, String url, Map<String, String> map, String fileName, List<String> files, ServiceCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        String logStr = new String(url);
        String urlStr = new String(url);
        urlStr += "?";
        logStr += "?";
        try {
            if (map != null) {
                Set<String> keys = map.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = i.next();
                    logStr += key + "=" + map.get(key) + "&";
//                    builder.addFormDataPart(key,map.get(key));

                    if(DESUtil.isContent(url)){
//                        logStr += key + "=" + DESUtil.encode(URLEncoder.encode(map.get(key), "utf-8"),DESUtil.KEY) + "&";
                        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                                RequestBody.create(null, DESUtil.encode(URLEncoder.encode(map.get(key), "utf-8"),DESUtil.KEY)));

                    }else{
//                        logStr += key + "=" + map.get(key) + "&";
                        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                                RequestBody.create(null, map.get(key)));
                    }


                }
            }
            if (null != files) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.size(); i++) {
                    File file = new File(files.get(i));
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\""),
                            fileBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder().url(urlStr).post(requestBody).build();
        Call call = mClientInstance.newCall(request);
        call.enqueue(okCallBack(callback, logStr));
    }

    public void httpPost(Context context, String url, Map<String, String> map, List<String> fileNames, List<String> files, ServiceCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        String logStr = new String(url);
        String urlStr = new String(url);
        urlStr += "?";
        logStr += "?";
        try {
            if (map != null) {
                Set<String> keys = map.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = i.next();
                    logStr += key + "=" + map.get(key) + "&";
//                    builder.addFormDataPart(key,map.get(key));

                    if(DESUtil.isContent(url)){
                        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                                RequestBody.create(null, DESUtil.encode(URLEncoder.encode(map.get(key), "utf-8"),DESUtil.KEY)));
                    }else{
                        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                                RequestBody.create(null, map.get(key)));
                    }


                }
            }
            if (null != files) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.size(); i++) {
                    File file = new File(files.get(i));
                    String fileName = fileNames.get(i);
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\""),
                            fileBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder().url(urlStr).post(requestBody).build();
        Call call = mClientInstance.newCall(request);
        call.enqueue(okCallBack(callback, logStr));
    }
    /**
     * 数据返回回调
     *
     * @param callback
     * @param logStr
     * @return
     */
    public Callback okCallBack(final ServiceCallback callback, final String logStr) {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(e, callback, logStr);

            }

            @Override
            public void onResponse(Response response) throws IOException {
                 String string =response.body().string();

                String str ="";
                if(string.contains("status")){
                    str = string;
                }else{
                    try {
                        Log.e("测试",string);
                        Log.e("测试",string.substring(1,string.length()-1));
                        str = DESUtil.decode(string.substring(1,string.length()-1),DESUtil.KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendSuccessResultCallback(str, callback, logStr);
            }
        };
    }


    /**
     * 回调失败异步处理
     *
     * @param e
     * @param callback
     * @param logStr
     */
    private void sendFailedStringCallback(final Exception e, final ServiceCallback callback, final String logStr) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    if (e != null) {
                        Lg.i("网络数据错误:", e.getMessage() + "1");
                    }
                    callback.error("网络请求失败");
                    Helper.showToast("网络请求失败");
                    Lg.i("请求url:", "blue:" + logStr);
                }
            }
        });
    }

    /**
     * 回调成功异步处理
     *
     * @param object
     * @param callback
     * @param logStr
     */
    private void sendSuccessResultCallback(final Object object, final ServiceCallback callback, final String logStr) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    Lg.i("请求url:", "blue:" + logStr);
                    final String htmlStr = object.toString();
                    Lg.i("返回结果:", "blue:" + htmlStr);
//                    semd( "blue:" + htmlStr);

                    //返回json格式数据
//				Lg.i("返回结果:", "blue:"+JsonFormatTool.formatJson(arg2));
                    BaseResult r = null;
                    try {
                        r = getGson().fromJson(htmlStr, callback.type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (r != null) {
                        if (r.status == BaseResult.STATUS_FAILE) {
                            callback.error(r.msg);
                            Lg.i("请求网速STATUS_FAILE:", Helper.getNetSpeedBytes()+"");
                        } else if (r.status == BaseResult.STATUS_SUCCESS) {
                            callback.done(0, r);
                            Lg.i("请求网速STATUS_SUCCESS:", Helper.getNetSpeedBytes()+"");
                        } else {
                            callback.error("网络数据错误");
                            Lg.i("请求网速:", Helper.getNetSpeedBytes()+"");
                        }
                    } else {
                        callback.error("网络数据错误");
                        Lg.i("请求网速r==null:", Helper.getNetSpeedBytes()+"");
                    }
                }
            }
        });
    }


    /**
     * 提供默认日期解析格式的Gson对象
     *
     * @return
     */

    private static Gson getGson() {
        return new GsonBuilder().setDateFormat(DefaultDateFormat).create();
    }

    private static final String DefaultDateFormat = "yyyy/MM/dd HH:mm:ss";


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * https 支持
     *
     * @return
     */
    private SSLContext getSslInfo() {

        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }


    /**
     * 获取文件mime类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
//
//    Message message;
//    /**
//     * 以HTML格式发送邮件
//     *
//     * @param msg
//     * @author爱编程的时小光 msg为你要发送的内容
//     */
//    public  void sendMail(String msg) {
//        try {
//            MailSenderInfo mailInfo = new MailSenderInfo();
////            mailInfo.setMailServerHost("smtp.exmail.qq.com");//发送邮件服务器地址
//            mailInfo.setMailServerHost("smtp.163.com");//发送邮件服务器地址
//            mailInfo.setMailServerPort("465");//端口号
//            mailInfo.setValidate(true);
//            mailInfo.setUserName("13973808975@163.com"); // 你的邮箱地址
//            mailInfo.setPassword("0117jiangshihua");// 您的邮箱密码，我就用xxx代替了
//            mailInfo.setFromAddress("13973808975@163.com"); // 发送的邮箱
//            mailInfo.setToAddress("1647150745@qq.com"); // 发到哪个邮件去
//            mailInfo.setSubject("爱编程的时小光"); // 邮件主题
//            mailInfo.setContent(msg); // 邮件文本
//            // 这个类主要来发送邮件
//            SimpleMailSender sms = new SimpleMailSender();
//            message = sms.sendTextMail(mailInfo);// 发送文体格式
//            //sms.sendHtmlMail(mailInfo);//发送html格式
//            handler.sendEmptyMessage(3);//handler处理发送邮件的网络线程
//        } catch (Exception e) {
//            Log.e("==SendMail", e.getMessage(), e);
//        }
//    }
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            new Thread(){
//                public void run(){
//                    try {
//                        Transport.send(message);
//                        Log.d("send","chenggong");
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }
//    };
//
//    private void semd(final String str){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                sendMail(str);
//            }
//        }).start();
//    }
}


package com.xd.refresh.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Proxy;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;

import com.xd.refresh.bean.AdInfo;
import com.xd.refresh.bean.DeviceInfo;
import com.xd.refresh.bean.ProxyIpBean;
import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.SkipManager;
import com.xd.refresh.manager.db.DeviceDBManager;

import org.apache.http.HttpHost;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Tools {

    /**
     * 获取更多芝麻代理ip信息
     */
    public static List<ProxyIpBean> getProxyIpList() {
        // 芝麻代理
//		String result = NetUtil.sendPost("http://http.zhimadaili.com/getip?",
//				"num=5&type=2&pro=&yys=0&port=1&time=1");
//		List<ProxyIpBean> ipBeans = ParseUtil.parseIpBeans(result);

        // 讯代理
        String result = NetUtil.sendGet("http://www.xdaili.cn/ipagent/privateProxy/applyStaticProxy",
                "count=1&spiderId=4fb3cc493e0340e98804629db8f4f0bd&returnType=2");
        List<ProxyIpBean> ipBeans = ParseUtil.parseIpBeansFromXun(result);
        System.out.println("获取到代理ip的条数---->>"+ipBeans.size());
        return ipBeans;

    }


    /**
     * 控制概率
     * @param proValue
     * @return
     */
    public static boolean isClick(int proValue) {
        int common = new Random().nextInt(100);
        if(common<=proValue) {
            return true;
        }
        return false;
    }

    /**
     * 普通广告点击事件处理
     *
     * @param adInfo
     * @param ipBean
     */
    public static void doClick(final AdInfo adInfo, final ProxyIpBean ipBean, DeviceInfo deviceInfo, String threadName) {
        System.out.println("广告类型-->>"+adInfo.getAdType() + "--线程编号----->>"+threadName);
        int length = adInfo.getClickUrls().length;
        for (int i = 0; i < length; i++) {
            String url = Tools.replaceAdClickUrl(adInfo.getClickUrls()[i], deviceInfo.adw,deviceInfo.adh);
            NetUtil.requestUrlByProxy(ipBean, url,deviceInfo.ua);
        }
//        // 测试
//        adInfo.setAdType(AdInfo.AD_TYPE_REDIRECT);
        if(TextUtils.equals(adInfo.getAdType(), AdInfo.AD_TYPE_REDIRECT)) {
            System.out.println("是跳转类型的广告！！！");
            if(isClick(20)){
                // 在点击概率下，再控制百分之而二十的概率
                // 需要二跳的概率
                ipBean.skipUrl = adInfo.getLandingUrl();
                SkipManager.getInstance().add(ipBean);
            }

//            ipBean.skipUrl = "http://www.baidu.com";
//            WebViewActivity.startActivity("http://www.baidu.com",ipBean.ip,ipBean.port);
//            Handler handler = new Handler();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    WebViewActivity.startActivity("wwww.baidu.com",ipBean.ip,ipBean.port);
//                }
//            });

        }else if (TextUtils.equals(adInfo.getAdType(), AdInfo.AD_TYPE_DOWNLOAD)) {
            // TODO 下载类型的广告
            System.out.println("是下载类型的广告！！！");
            // 上报讯飞开始下载的链接
            int length2 = adInfo.getInstDownloadStartUrls().length;
            System.out.println("上报开始下载！！");
            for (int i = 0; i < length2; i++) {
                NetUtil.requestUrlByProxy(ipBean, adInfo.getInstDownloadStartUrls()[i],deviceInfo.ua);
            }

            try {
                // 休眠一两秒
                Thread.sleep(Tools.randomMinMax(1000,2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            // 开始下载
//            try {
//                NetUtil.downLoadFromUrl(adInfo.getLandingUrl(), threadName + ".apk", "c:/download");
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            System.out.println("上报下载完成！！");
            // 上报讯飞开始下载完成的链接
            int length3 = adInfo.getInstDownloadSuccUrls().length;
            for (int i = 0; i < length3; i++) {
                NetUtil.requestUrlByProxy(ipBean, adInfo.getInstDownloadSuccUrls()[i],deviceInfo.ua);
            }

            System.out.println("上报开始安装！！");
            // 上报讯飞开始安装的链接
            int length4 = adInfo.getInstInstallStartUrls().length;
            for (int i = 0; i < length4; i++) {
                NetUtil.requestUrlByProxy(ipBean, adInfo.getInstInstallStartUrls()[i],deviceInfo.ua);
            }

            // 休眠2秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("上报安装完成！！");
            // 上报讯飞安装成功的链接
            int length5 = adInfo.getInstInstallSuccUrls().length;
            for (int i = 0; i < length5; i++) {
                NetUtil.requestUrlByProxy(ipBean, adInfo.getInstInstallSuccUrls()[i],deviceInfo.ua);
            }
        }else if (TextUtils.equals(adInfo.getAdType(), AdInfo.AD_TYPE_BRAND)) {
            // TODO 品牌类型的广告
            System.out.println("是品牌类型的广告！！！");
        }
    }


    /**
     * 替换科大讯飞中的click_url
     *
     * @param url
     * @return
     */
    public static String replaceAdClickUrl(String url,int adWidth,int adHeight) {
//        int width = showAdWidthAndHeight[0];
//        int height = showAdWidthAndHeight[1];
        float downX = adWidth * 0.456f;
        float downY = adHeight * 0.525f;
        float upX = downX + 0.643f;
        float upY = downY - 0.473f;
        url = url.replace("IT_CLK_PNT_DOWN_X", String.valueOf(downX));
        url = url.replace("IT_CLK_PNT_DOWN_Y", String.valueOf(downY));
        url = url.replace("IT_CLK_PNT_UP_X", String.valueOf(upX));
        url = url.replace("IT_CLK_PNT_UP_Y", String.valueOf(upY));
        return url;
    }

    /**
     * 获取数值之间的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomMinMax(int min, int max) {
        if (min >= max) {
            return min;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    /**
     * 模拟Mac地址
     *
     * @return
     */
    public static String simulationMac() {
        char[] char1 = "abcdef".toCharArray();
        char[] char2 = "0123456789".toCharArray();
        StringBuffer mBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int t = new java.util.Random().nextInt(char1.length);
            int y = new java.util.Random().nextInt(char2.length);
            int key = new java.util.Random().nextInt(2);
            if (key == 0) {
                mBuffer.append(char2[y]).append(char1[t]);
            } else {
                mBuffer.append(char1[t]).append(char2[y]);
            }

            if (i != 5) {
                mBuffer.append(":");
            }
        }
        return mBuffer.toString();
    }

    /**
     * 随机从数据库中获取数据
     * @return
     */
    public static DeviceInfo getDeviceInfoByRamdon(){
        int id = randomMinMax(1, Constance.DB_TOTAL_DATA_COUNT);
        return DeviceDBManager.getInstance().queryDeviceInfo(id);
//        DeviceInfo deviceInfo = DeviceDBManager.getInstance().queryDeviceInfo(id);
//        if(deviceInfo != null){
//            // 查询到数据了
//            deviceInfo.mac = simulationMac();
//            deviceInfo.osVersion = Constance.OS_VERSIONS[Tools.randomMinMax(0, Constance.OS_VERSIONS.length - 1)];
//            deviceInfo.userAgent = Constance.USER_AGENTS[Tools.randomMinMax(0, Constance.USER_AGENTS.length - 1)].replace("OS_VERSION", deviceInfo.osVersion);
//
//            deviceInfo.language = "zh-CN";
//            deviceInfo.deviceType = 0;
//            deviceInfo.density = Constance.DENSITY[Tools.randomMinMax(0, Constance.DENSITY.length - 1)];
//            // 随机获取 运营商编号
//            deviceInfo.operator = Constance.OPERATORS[Tools.randomMinMax(0, Constance.OPERATORS.length - 1)];
//            deviceInfo.net = 2;
//            int[] screen_ressolution = Constance.SCREEN_RESSOLUTION[Tools.randomMinMax(0, Constance.SCREEN_RESSOLUTION.length - 1)];
//            deviceInfo.deviceScreenWidth = screen_ressolution[0];
//            deviceInfo.deviceScreenHeight = screen_ressolution[1];
//            deviceInfo.orientation = 0;
//
//            // TODO model 和 vendor的值需要处理
//            deviceInfo.model = "meetuu%20G7";
//            deviceInfo.vendor = "HuaWei";
//        }
//        return deviceInfo;
    }



    /**
     * 设置WebView的代理
     * 注意这里applicationClassName 传递的是 application 的类名
     */
    public static boolean setWebViewProxy(WebView webview, String host, int port, String applicationClassName) {
        // 3.2 (HC) or lower
        if (Build.VERSION.SDK_INT <= 13) {
            return setProxyUpToHC(webview, host, port);
        }
        // ICS: 4.0
        else if (Build.VERSION.SDK_INT <= 15) {
            return setProxyICS(webview, host, port);
        }
        // 4.1-4.3 (JB)
        else if (Build.VERSION.SDK_INT <= 18) {
            return setProxyJB(webview, host, port);
        }
        // 4.4 (KK) & 5.0 (Lollipop)
        else {
            return setProxyKKPlus(webview, host, port, applicationClassName);
        }
    }


    /**
     * 关闭代理
     * @param webview
     * @param applicationClassName
     * @return
     */
    public static boolean revertBackProxy(WebView webview, String applicationClassName) {
        // 3.2 (HC) or lower
        if (Build.VERSION.SDK_INT <= 13) {
            return true;
        }
        // ICS: 4.0
        else if (Build.VERSION.SDK_INT <= 15) {
            return revertProxyICS(webview);
        }
        // 4.1-4.3 (JB)
        else if (Build.VERSION.SDK_INT <= 18) {
            return revertProxyJB(webview);
        }
        // 4.4 (KK) & 5.0 (Lollipop)
        else {
            return revertProxyKKPlus(webview, applicationClassName);
        }
    }

    /**
     * Set Proxy for Android 3.2 and below.
     */
    @SuppressWarnings("all")
    private static boolean setProxyUpToHC(WebView webview, String host, int port) {
        Log.d("llj", "Setting proxy with <= 3.2 API.");

        HttpHost proxyServer = new HttpHost(host, port);
        // Getting network
        Class networkClass = null;
        Object network = null;
        try {
            networkClass = Class.forName("android.webkit.Network");
            if (networkClass == null) {
                Log.e("llj", "failed to get class for android.webkit.Network");
                return false;
            }
            Method getInstanceMethod = networkClass.getMethod("getInstance", Context.class);
            if (getInstanceMethod == null) {
                Log.e("llj", "failed to get getInstance method");
            }
            network = getInstanceMethod.invoke(networkClass, new Object[]{webview.getContext()});
        } catch (Exception ex) {
            Log.e("llj", "error getting network: " + ex);
            return false;
        }
        if (network == null) {
            Log.e("llj", "error getting network: network is null");
            return false;
        }
        Object requestQueue = null;
        try {
            Field requestQueueField = networkClass
                    .getDeclaredField("mRequestQueue");
            requestQueue = getFieldValueSafely(requestQueueField, network);
        } catch (Exception ex) {
            Log.e("llj", "error getting field value");
            return false;
        }
        if (requestQueue == null) {
            Log.e("llj", "Request queue is null");
            return false;
        }
        Field proxyHostField = null;
        try {
            Class requestQueueClass = Class.forName("android.net.http.RequestQueue");
            proxyHostField = requestQueueClass
                    .getDeclaredField("mProxyHost");
        } catch (Exception ex) {
            Log.e("llj", "error getting proxy host field");
            return false;
        }

        boolean temp = proxyHostField.isAccessible();
        try {
            proxyHostField.setAccessible(true);
            proxyHostField.set(requestQueue, proxyServer);
        } catch (Exception ex) {
            Log.e("llj", "error setting proxy host");
        } finally {
            proxyHostField.setAccessible(temp);
        }

        Log.d("llj", "Setting proxy with <= 3.2 API successful!");
        return true;
    }

    @SuppressWarnings("all")
    private static boolean setProxyICS(WebView webview, String host, int port) {
        try {
            Log.d("llj", "Setting proxy with 4.0 API.");

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Class wv = Class.forName("android.webkit.WebView");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));

            Log.d("llj", "Setting proxy with 4.0 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e("llj", "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    private static boolean revertProxyICS(WebView webview) {
        try {
            Log.d("llj", "Setting proxy with 4.0 API.");

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Class wv = Class.forName("android.webkit.WebView");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Object o = null;
            updateProxyInstance.invoke(sJavaBridge, o);

            Log.d("llj", "Setting proxy with 4.0 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e("llj", "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    /**
     * Set Proxy for Android 4.1 - 4.3.
     */
    @SuppressWarnings("all")
    private static boolean setProxyJB(WebView webview, String host, int port) {
        Log.d("llj", "Setting proxy with 4.1 - 4.3 API.");

        try {
            Class wvcClass = Class.forName("android.webkit.WebViewClassic");
            Class wvParams[] = new Class[1];
            wvParams[0] = Class.forName("android.webkit.WebView");
            Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
            Object webViewClassic = fromWebView.invoke(null, webview);

            Class wv = Class.forName("android.webkit.WebViewClassic");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));
        } catch (Exception ex) {
            Log.e("llj", "Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }

        Log.d("llj", "Setting proxy with 4.1 - 4.3 API successful!");
        return true;
    }

    private static boolean revertProxyJB(WebView webview) {
        Log.d("llj", "revert proxy with 4.1 - 4.3 API.");

        try {
            Class wvcClass = Class.forName("android.webkit.WebViewClassic");
            Class wvParams[] = new Class[1];
            wvParams[0] = Class.forName("android.webkit.WebView");
            Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
            Object webViewClassic = fromWebView.invoke(null, webview);

            Class wv = Class.forName("android.webkit.WebViewClassic");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Object o = null;
            updateProxyInstance.invoke(sJavaBridge, o);
        } catch (Exception ex) {
            Log.e("llj", "Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }

        Log.d("llj", "revert proxy with 4.1 - 4.3 API successful!");
        return true;
    }

    // from https://stackoverflow.com/questions/19979578/android-webview-set-proxy-programatically-kitkat
    @SuppressLint("NewApi")
    @SuppressWarnings("all")
    private static boolean setProxyKKPlus(WebView webView, String host, int port, String applicationClassName) {
        Log.d("llj", "Setting proxy with >= 4.4 API.");

        Context appContext = webView.getContext().getApplicationContext();
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
        try {
            Class applictionCls = Class.forName(applicationClassName);
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);

                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }

            Log.d("llj", "Setting proxy with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (NoSuchFieldException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (IllegalArgumentException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (NoSuchMethodException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        }
        return false;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("all")
    private static boolean revertProxyKKPlus(WebView webView, String applicationClassName) {

        Context appContext = webView.getContext().getApplicationContext();
        Properties properties = System.getProperties();

        properties.remove("http.proxyHost");
        properties.remove("http.proxyPort");
        properties.remove("https.proxyHost");
        properties.remove("https.proxyPort");
        try {
            Class applictionCls = Class.forName(applicationClassName);
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);
//                        intent.putExtra("proxy", null);
                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }
            Log.d("llj", "Revert proxy with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (NoSuchFieldException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (IllegalArgumentException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (NoSuchMethodException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("llj", e.getMessage());
            Log.v("llj", exceptionAsString);
        }
        return false;
    }

    private static Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException {
        boolean oldAccessibleValue = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(classInstance);
        field.setAccessible(oldAccessibleValue);
        return result;
    }


    /**
     * 获取屏幕的宽度和高度
     * @param context
     */
    public static void getScreenWidhtAndHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Constance.SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
        Constance.SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();

        Log.i("llj","屏幕宽度----->>>"+Constance.SCREEN_WIDTH);
        Log.i("llj","屏幕高度----->>>"+Constance.SCREEN_HEIGHT);
    }

}


package com.xd.refresh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xd.refresh.bean.ProxyIpBean;
import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.SkipManager;
import com.xd.refresh.util.Tools;

/**
 * 网页类 webview(普通的网页类)
 *
 * @author lilijun
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    private String loadUrl = "";

    private String host;

    private int port;

    private String userAgent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
        webView = new WebView(this);
        setContentView(webView);


        loadUrl = getIntent().getStringExtra("loadUrl");
        host = getIntent().getStringExtra("host");
        port = getIntent().getIntExtra("port", -1);
        userAgent = getIntent().getStringExtra("userAgent");

        if (port < 0) {
            finish();
            return;
        }
        Tools.setWebViewProxy(webView, host, port, AppApplication.class.getName());

        // loadUrl = "http://www.baidu.com";
        webView.loadUrl(loadUrl);

        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // 返回值是true的时候就在本webView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("llj", "界面开始加载！！！");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("llj", "界面加载完成！！！");
                moniTouch();

//                finish();
            }
        });

//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    // 网页加载完成
//                    Log.i("llj", "网页加载完成！！！-->>>"+newProgress);
//                    moniTouch();
//                    finish();
//                } else {
//                    // 加载中
//                    Log.i("llj", "网页加载中 ---->> " + newProgress);
//                }
//            }
//
//        });


        WebSettings settings = webView.getSettings();
        // 设置webview的userAgent
        settings.setUserAgentString(userAgent);
        // 启用支持javascript
        settings.setJavaScriptEnabled(true);
        // // 优先使用缓存
        // settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 不使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);

        // //设置字符编码
        // settings.setDefaultTextEncodingName("GBK");
        // 是否支持网页缩放
        // settings.setBuiltInZoomControls(true);
        // settings.setSupportZoom(true);

//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "android");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            // 返回上一页面
            webView.goBack();
        } else {
            // 按返回退出界面无效
//            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
//        SkipManager.getInstance().doneOnce();
        // 关闭代理
        Tools.revertBackProxy(webView, AppApplication.class.getName());
    }


    public static void startActivity(String url, String host, int port,String userAgent) {
        Context context = AppApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, WebViewActivity.class);
        if ("".equals(url.trim())) {
            return;
        }
        intent.putExtra("loadUrl", url);
        intent.putExtra("host", host);
        intent.putExtra("port", port);
        intent.putExtra("userAgent",userAgent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 模拟点击
     */
    private void moniTouch() {
        float downX = Tools.randomMinMax(0, Constance.SCREEN_WIDTH);
        downX += (float) Tools.randomMinMax(0, 999) / 1000;

        Log.i("llj", "downX----->>>" + downX);

        float downY = Tools.randomMinMax(0, Constance.SCREEN_HEIGHT);
        downY += (float) Tools.randomMinMax(0, 999) / 1000;

        Log.i("llj", "downY----->>>" + downY);


//        float upX = downX - (float)Tools.randomMinMax(0,286)/1000;
//        float upY = downY + (float)Tools.randomMinMax(0,213)/1000;
//
//        Log.i("llj","upX----->>>"+upX);
//        Log.i("llj","upY ----->>>"+upY);


        final long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(
                downTime, downTime, MotionEvent.ACTION_DOWN, downX, downY, 0);
        final MotionEvent upEvent = MotionEvent.obtain(
                downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, downX, downY, 0);
        //添加到webview_loading_round_iv上
        webView.onTouchEvent(downEvent);
        webView.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();


        // 检查进入下一个跳转信息
        loadNext();
//        Tools.revertBackProxy(webView, AppApplication.class.getName());
//        SkipManager.getInstance().doneOnce();
    }


    /**
     * 加载下一条网页
     */
    public void loadNext() {
        ProxyIpBean ipBean = SkipManager.getInstance().getNextIpBean();
        if (ipBean == null) {
            return;
        }
        loadUrl = ipBean.skipUrl;
        host = ipBean.ip;
        port = ipBean.port;

        Tools.setWebViewProxy(webView, host, port, AppApplication.class.getName());
        webView.loadUrl(loadUrl);
    }


}

package com.xd.refresh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xd.refresh.bean.ProxyIpBean;
import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.SkipManager;
import com.xd.refresh.util.Tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 网页类 webview(普通的网页类)
 *
 * @author lilijun
 */
public class WebViewActivity extends AppCompatActivity {
    private  WebView webView;

//    private String loadUrl = "";

//    private String host;
//
//    private int port;

//    private String userAgent;

    private ProxyIpBean proxyIp;


    private long timeout = 10000;

    private Timer timer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //这里对已经显示出页面且加载超时的情况不做处理
                if (webView != null && webView.getProgress() < 100) {
                    // 超时了
                    Log.e("llj", "加载超时了--url---->>" + webView.getUrl());
                    loadNext();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("llj","onNewIntent run here!!!");
        initData();
    }

    private void initData(){
        proxyIp = (ProxyIpBean) getIntent().getSerializableExtra("proxyIp");
        if(proxyIp == null){
            return;
        }
        if(TextUtils.isEmpty(proxyIp.skipUrl)){
            return;
        }
        // 设置WebView 代理
        Tools.setWebViewProxy(webView, proxyIp.ip, proxyIp.port, AppApplication.class.getName());
        // 设置webview的userAgent
        webView.getSettings().setUserAgentString(proxyIp.userAgent);

        webView.loadUrl(proxyIp.skipUrl);
    }



    protected void initView() {
//        FrameLayout frameLayout = new FrameLayout(WebViewActivity.this);
//        webView = new WebView(this);
//        Button testBtn = new Button(WebViewActivity.this);
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ProxyIpBean ipBean = new ProxyIpBean();
//                ipBean.skipUrl = "http://www.baidu.com";
//
//                ProxyIpBean ipBean2 = new ProxyIpBean();
//                ipBean2.skipUrl = "http://www.voiceads.cn/";
//
//                ProxyIpBean ipBean3 = new ProxyIpBean();
//                ipBean3.skipUrl = "http://member.djjlll.com/shop.html#/line/share";
//
//                ProxyIpBean ipBean4 = new ProxyIpBean();
//                ipBean4.skipUrl = "http://json.cn/";
//
//                ProxyIpBean ipBean5 = new ProxyIpBean();
//                ipBean5.skipUrl = "http://blog.csdn.net/jack__frost/article/details/52965905";
//
//                ProxyIpBean ipBean6 = new ProxyIpBean();
//                ipBean6.skipUrl = "http://www.jianshu.com/p/0fcf6a1a13fe";
//
//                ProxyIpBean ipBean7 = new ProxyIpBean();
//                ipBean7.skipUrl = "http://www.cnblogs.com/wjtaigwh/p/6043829.html";
//
//
//                SkipManager.getInstance().add(ipBean);
//                SkipManager.getInstance().add(ipBean2);
//                SkipManager.getInstance().add(ipBean3);
//                SkipManager.getInstance().add(ipBean4);
//                SkipManager.getInstance().add(ipBean5);
//                SkipManager.getInstance().add(ipBean6);
//                SkipManager.getInstance().add(ipBean7);
//            }
//        });
//        testBtn.setText("加载新的数据");
//        frameLayout.addView(webView);
//        frameLayout.addView(testBtn,new FrameLayout.LayoutParams(200,100));

        webView = new WebView(this);
        setContentView(webView);




        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("llj", "界面开始加载！！！");
                timer = new Timer();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        //超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        timer.cancel();
                        timer.purge();
                    }
                };
                timer.schedule(tt, timeout);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (view.getProgress() >= 100) {
                    Log.i("llj", "界面加载完成！！！progress----->>>" + view.getProgress());
                    timer.cancel();
                    timer.purge();

                    moniTouch();
                }
            }
        });

        // 启用支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 不使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLoadWithOverviewMode(true);
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


    public static void startActivity(ProxyIpBean ipBean) {
        Context context = AppApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("proxyIp",ipBean);
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
        ProxyIpBean ipBean = SkipManager.getInstance().getNextIpBean(true);
        if (ipBean == null) {
            Log.i("llj", "获取到下一次的加载信息为  null");
            return;
        }

        proxyIp = ipBean;

        webView.clearCache(true);
        // 设置webview代理
        Tools.setWebViewProxy(webView, proxyIp.ip, proxyIp.port, AppApplication.class.getName());
        // 设置webview的userAgent
        webView.getSettings().setUserAgentString(proxyIp.userAgent);
        Log.i("llj", "加载下一个网页链接!!!! url------->>" + proxyIp.skipUrl);
        webView.loadUrl(proxyIp.skipUrl);
    }

}

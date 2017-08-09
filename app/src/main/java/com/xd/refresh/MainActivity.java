package com.xd.refresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.thread.AdTask;
import com.xd.refresh.manager.thread.ThreadPoolManager;
import com.xd.refresh.util.Tools;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.getScreenWidhtAndHeight(MainActivity.this);

        Button testBtn = (Button) findViewById(R.id.begin_test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<ProxyIpBean> ipBeans = Tools.getProxyIpList();
//                        Log.i("llj","ipBeans.size()---->>>"+ipBeans.size());
//                    }
//                }).start();

                AdTask adTask1 = new AdTask(Constance.AXF_AD_UNIT_IDS, Constance.AXF_APP_ID, Constance.AXF_APP_NAME, Constance.AXF_APP_PACKAGE_NAME, 1);
//                AdTask adTask2 = new AdTask(Constance.AXF_AD_UNIT_IDS, Constance.AXF_APP_ID, Constance.AXF_APP_NAME, Constance.AXF_APP_PACKAGE_NAME, 2);
//                AdTask adTask3 = new AdTask(Constance.AXF_AD_UNIT_IDS, Constance.AXF_APP_ID, Constance.AXF_APP_NAME, Constance.AXF_APP_PACKAGE_NAME, 3);
//		AdTask adTask4 = new AdTask(Constance.AXF_AD_UNIT_IDS, Constance.AXF_APP_ID, Constance.AXF_APP_NAME, Constance.AXF_APP_PACKAGE_NAME, 4);
//		AdTask adTask5 = new AdTask(Constance.AXF_AD_UNIT_IDS, Constance.AXF_APP_ID, Constance.AXF_APP_NAME, Constance.AXF_APP_PACKAGE_NAME, 5);

                ThreadPoolManager.getInstance().addTask(adTask1);
//                ThreadPoolManager.getInstance().addTask(adTask2);
//                ThreadPoolManager.getInstance().addTask(adTask3);
//		ThreadPoolManager.getInstance().addTask(adTask4);
//		ThreadPoolManager.getInstance().addTask(adTask5);


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
            }
        });
    }
}

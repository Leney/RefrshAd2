package com.xd.refresh.manager;

import com.xd.refresh.WebViewActivity;
import com.xd.refresh.bean.ProxyIpBean;
import com.xd.refresh.util.LinkListHelper;

/**
 * 跳转界面管理器
 * Created by llj on 2017/7/18.
 */

public class SkipManager {


    private LinkListHelper<ProxyIpBean> list;

    private static SkipManager instance;

    private SkipManager() {
        list = new LinkListHelper<>();
    }


    public static SkipManager getInstance() {
        if (instance == null) {
            synchronized (SkipManager.class) {
                if (instance == null) {
                    instance = new SkipManager();
                }
            }
        }
        return instance;
    }

    public LinkListHelper getLinkedList() {
        return list;
    }


    public synchronized void add(ProxyIpBean ipBean) {
        if (list.isEmpty()) {
            list.add(ipBean);
            WebViewActivity.startActivity(ipBean);
//            if(AppApplication.isTopActivity("WebViewActivity")){
//                Log.i("llj","当前顶部Activity是WebViewActivity!!");
////                WebViewActivity.beginLoadUrl();
//                WebViewActivity.startActivity(ipBean);
//            }else {
//                WebViewActivity.startActivity(ipBean);
//            }
        } else {
            list.add(ipBean);
        }
    }

//    public synchronized void doneOnce() {
//        list.removeFirst();
//        if (!list.isEmpty()) {
//            ProxyIpBean ipBean = list.getFirst();
//            WebViewActivity.startActivity(ipBean.skipUrl, ipBean.ip, ipBean.port,ipBean.userAgent);
//        }
//    }

    /**
     * 获取下一次需要加载的网页路径、代理等信息
     *
     * @param isRemoveFirst 是否移除第一条信息
     * @return
     */
    public synchronized ProxyIpBean getNextIpBean(boolean isRemoveFirst) {
        if (isRemoveFirst) {
            list.removeFirst();
        }
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }
}

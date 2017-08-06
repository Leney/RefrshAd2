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
            WebViewActivity.startActivity(ipBean.skipUrl, ipBean.ip, ipBean.port);
        } else {
            list.add(ipBean);
        }
    }

    public synchronized void doneOnce() {
        list.removeFirst();
        if (!list.isEmpty()) {
            ProxyIpBean ipBean = list.getFirst();
            WebViewActivity.startActivity(ipBean.skipUrl, ipBean.ip, ipBean.port);
        }
    }


}

package com.xd.refresh.util.listener;

import com.xd.refresh.bean.DeviceInfo;
import com.xd.refresh.bean.ProxyIpBean;

import org.json.JSONObject;

/**
 * 讯飞广告请求网络监听器
 * @author dell
 *
 */
public interface OnLoadAdListener {
    /**
     * 加载成功(异步方法)
     */
    void onLoadSuccess(JSONObject resultObject, ProxyIpBean ipBean,DeviceInfo deviceInfo);

    /**
     * 加载失败(异步方法)
     */
    void onLoadFailed(String msg);

    /**
     * 网络错误(异步方法)
     */
    void onNetError(String msg);
}

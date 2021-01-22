package com.jz.dddad;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.huawei.hms.ads.HwAds;
import com.jz.dddad.google.AppOpenManager;

/**
 * 默认对华为google广告都做了初始化 如果不需要 可以不继承此类
 */
public class ADApp extends Application {
    public static AppOpenManager appOpenManager;
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);
        appOpenManager =  new AppOpenManager(this); // 开屏广告
//        华为广告初始化
        HwAds.init(this);
    }
}

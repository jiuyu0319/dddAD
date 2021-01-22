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
    public static boolean isHuawei = false;
    @Override
    public void onCreate() {
        super.onCreate();

        String brand = android.os.Build.BRAND;
        if (brand.equals("Huawei")||brand.equals("HUAWEI")||brand.equals("HONOR")){
            HwAds.init(this);
            isHuawei = true;
        } else {
            MobileAds.initialize(this);
            appOpenManager =  new AppOpenManager(this); // 开屏广告
            isHuawei = false;
        }
    }
}

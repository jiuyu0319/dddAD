package com.jz.dddad.huawei;

import android.content.Context;
import android.util.Log;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.jz.dddad.OnAdErrorAndClosed;

public class HuaweiInterstitialAd {
    private InterstitialAd interstitialAd;
    public void init(Context context, OnAdErrorAndClosed adErrorAndClosed){
        init(context,"testb4znbuh3n2",adErrorAndClosed);
    }
    public void init(Context context,String code,OnAdErrorAndClosed adErrorAndClosed){
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdId(code);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // 广告获取成功时调用
                 //showInterstitialAd();
            }
            @Override
            public void onAdFailed(int errorCode) {
                // 广告获取失败时调用
                Log.d("华为插页广告加载失败","errcode  :"+errorCode);
                adErrorAndClosed.errorAndClosed();
             }
            @Override
            public void onAdClosed() {
                // 广告关闭时调用
                Log.d("华为插页广告g关闭","onAdClosed  :");

                adErrorAndClosed.errorAndClosed();
                interstitialAd.loadAd(adParam);
            }
            @Override
            public void onAdClicked() {
                // 广告点击时调用
             }
            @Override
            public void onAdLeave() {
                //广告离开时调用
             }
            @Override
            public void onAdOpened() {
                // 广告打开时调用
             }
            @Override
            public void onAdImpression() {
                // 广告曝光时调用
             }});
        interstitialAd.loadAd(adParam);

    }

    public void showInterstitialAd() {
        // 显示广告
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d("华为插页广告","Ad did not load");
        }
    }
}

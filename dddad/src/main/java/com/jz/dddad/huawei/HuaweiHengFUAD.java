package com.jz.dddad.huawei;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.FrameLayout;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.jz.dddad.OnAdErrorAndClosed;

public class HuaweiHengFUAD {

    public void getBannerView(FrameLayout adFrameLayout, Context context){
        getBannerView(adFrameLayout,context,"testw6vs28auh3",null);
    }

    public void getBannerView(FrameLayout adFrameLayout, Context context, OnAdErrorAndClosed adErrorAndClosed){
        getBannerView(adFrameLayout,context,"testw6vs28auh3",adErrorAndClosed);
    }


    public void getBannerView(FrameLayout adFrameLayout, Context context, String adid,OnAdErrorAndClosed adErrorAndClosed){

        BannerView bannerView = new BannerView(context);
        // Set an ad slot ID.
        bannerView.setAdId(adid);
        // Set the background color and size based on user selection.
        BannerAdSize adSize = BannerAdSize.BANNER_SIZE_320_50;
        bannerView.setBannerAdSize(adSize);

        bannerView.setBackgroundColor(Color.WHITE);
        adFrameLayout.addView(bannerView);
        bannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Called when an ad is loaded successfully.
            }

            @Override
            public void onAdFailed(int errorCode) {
                // Called when an ad fails to be loaded.

                Log.d("华为广告 onAdFailed",errorCode+"");
                if (adErrorAndClosed!= null)adErrorAndClosed.errorAndClosed();

            }

            @Override
            public void onAdOpened() {
                // Called when an ad is opened.
                //showToast(String.format("Ad opened "));
            }

            @Override
            public void onAdClicked() {
                // Called when a user taps an ad.
//            showToast("Ad clicked");
            }

            @Override
            public void onAdLeave() {
                // Called when a user has left the app.
                //showToast("Ad Leave");
            }

            @Override
            public void onAdClosed() {
                Log.d("华为广告 onAdClosed","onAdClosed");
                adErrorAndClosed.errorAndClosed();

            }
        } );
        bannerView.setBannerRefresh(30);
        bannerView.loadAd(new AdParam.Builder().build());
    }

}

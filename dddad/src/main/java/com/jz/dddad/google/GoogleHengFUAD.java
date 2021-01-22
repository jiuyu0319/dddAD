package com.jz.dddad.google;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jz.dddad.R;

public class GoogleHengFUAD {
    /**
     * 横幅测试广告id
     * @param frameLayout
     * @param activity
     */
    public void setBannerAd(FrameLayout frameLayout , Activity activity){
        setBannerAd(frameLayout,activity,activity.getString(R.string.google_test_bannerid));
    }
    /**
     * 横幅广告id
     * @param frameLayout
     * @param activity
     */
    public void setBannerAd(FrameLayout frameLayout, Activity activity, String googlecode){
        AdView adView = new AdView(activity);
        adView.setAdUnitId(googlecode);
        frameLayout.addView(adView);
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
        //AdSize adSize = getAdSize(activity);
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e(activity.getClass().getSimpleName(),"onAdFailedToLoad    :"+i);
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                Log.e(activity.getClass().getSimpleName(),"onAdLoaded    :");
                super.onAdLoaded();

            }
        });

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    /**
     * 横幅广告自己传入adlistener监听
     * @param frameLayout
     * @param activity
     * @param googlecode
     * @param adListener
     */
    public void setBannerAd(FrameLayout frameLayout, Activity activity, String googlecode,AdListener adListener){
        AdView adView = new AdView(activity);
        adView.setAdUnitId(googlecode);
        frameLayout.addView(adView);
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
        //AdSize adSize = getAdSize(activity);
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdListener(adListener);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

}

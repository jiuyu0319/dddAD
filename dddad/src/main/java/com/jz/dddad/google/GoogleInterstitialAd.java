package com.jz.dddad.google;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.jz.dddad.OnAdErrorAndClosed;
import com.jz.dddad.R;

public class GoogleInterstitialAd {
    private InterstitialAd mInterstitialAd;
    private OnAdErrorAndClosed onAdErrorAndClosed;
    private Activity activity;

    public GoogleInterstitialAd(Activity activity , OnAdErrorAndClosed onAdErrorAndClosed) {
        this.onAdErrorAndClosed = onAdErrorAndClosed;
        this.activity = activity;
        init(activity,onAdErrorAndClosed,activity.getString(R.string.google_test_interstitial));
    }

    public GoogleInterstitialAd(Activity activity,String code, OnAdErrorAndClosed onAdErrorAndClosed) {
        this.onAdErrorAndClosed = onAdErrorAndClosed;
        this.activity = activity;
        init(activity,onAdErrorAndClosed,code);
    }

    private void init(Activity activity, OnAdErrorAndClosed onAdErrorAndClosed, String code){
        this.onAdErrorAndClosed = onAdErrorAndClosed;
        this.activity = activity;
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(code);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onAdErrorAndClosed.errorAndClosed();
                Log.d("GoogleInterstitialAd  ","错误码 :"+loadAdError.getCode());
                // Code to be executed when an ad request fails.
            }


            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                onAdErrorAndClosed.errorAndClosed();

            }
        });
    }

    public void  showInterstitial(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            onAdErrorAndClosed.errorAndClosed();
            Log.d("GoogleInterstitialAd  ","showInterstitial 的时候 : if (mInterstitialAd != null && mInterstitialAd.isLoaded())");

        }
    }
}

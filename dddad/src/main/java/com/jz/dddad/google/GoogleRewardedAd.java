package com.jz.dddad.google;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.jz.dddad.OnAdErrorAndClosed;
import com.jz.dddad.R;

public class GoogleRewardedAd {
    private RewardedAd rewardedAd;
    private Activity activity;
    private String code;
    private OnAdErrorAndClosed adErrorAndClosed;
    private OnAdErrorAndClosed adclosed;
    public void init(Activity activity, OnAdErrorAndClosed adErrorAndClosed,OnAdErrorAndClosed adclosed){
        this.activity = activity;

        init(activity,activity.getResources().getString(R.string.google_test_rewardedad),adErrorAndClosed,adclosed);
    }

    public void init(Activity activity,String code,OnAdErrorAndClosed adErrorAndClosed,OnAdErrorAndClosed adclosed){
        this.code = code;
        this.adErrorAndClosed = adErrorAndClosed;
        this.adclosed = adclosed;
        rewardedAd = new RewardedAd(activity, code);

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.d("GoogleInterstitialAd  ","错误码 :"+adError.getCode());
                adErrorAndClosed.errorAndClosed();
            }

        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);


    }
    public void showAD(OnAdErrorAndClosed ok){
        rewardedAd.show(activity, new RewardedAdCallback() {
            @Override
            public void onRewardedAdOpened() {
                // Ad opened.
            }

            @Override
            public void onRewardedAdClosed() {
                // Ad closed.
                rewardedAd = createAndLoadRewardedAd();
                adErrorAndClosed.errorAndClosed();

            }

            @Override
            public void onUserEarnedReward(@NonNull RewardItem reward) {
                // User earned reward.
                adclosed.errorAndClosed();


            }

            @Override
            public void onRewardedAdFailedToShow(AdError adError) {
                // Ad failed to display.
            }
        });
    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(activity, code);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                Log.d("GoogleInterstitialAd  ","错误码 :"+adError.getCode());
                // Ad failed to load.
                adErrorAndClosed.errorAndClosed();

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }
}

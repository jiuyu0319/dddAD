package com.jz.dddad.huawei;

import android.app.Activity;
import android.util.Log;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.jz.dddad.OnAdErrorAndClosed;

public class HuaweiRewarded {


    private RewardAd rewardAd;
    private Activity activity;
    private OnAdErrorAndClosed no, ok;

    /**|
     *
     * @param context
     * @param no  关闭回调
     * @param ok  可领取奖励回调
     * @return
     */
    public HuaweiRewarded init(Activity context, OnAdErrorAndClosed no, OnAdErrorAndClosed ok) {
        init(context, "testx9dtjwj8hp", no, ok);
        return this;
    }
    /**|
     *
     * @param context
     * @param no  关闭回调
     * @param ok  可领取奖励回调
     * @return
     */
    public HuaweiRewarded init(Activity context, String code, OnAdErrorAndClosed no, OnAdErrorAndClosed ok) {
        this.activity = context;
        this.no = no;
        this.ok = ok;
        if (rewardAd == null) {
            rewardAd = new RewardAd(context, code);
        }

        rewardAd.loadAd(new AdParam.Builder().build(), listener);
        return this;
    }
    public HuaweiRewarded rewardAdShow() {
        rewardAdShow(null);
        return this;
    }
    public HuaweiRewarded rewardAdShow(OnAdErrorAndClosed oks) {
        if (rewardAd.isLoaded()) {
            rewardAd.show(activity, new RewardAdStatusListener() {
                @Override
                public void onRewardAdOpened() {
                    // 激励广告被打开
                    if (oks!= null)oks.errorAndClosed();

                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    // 激励广告展示失败
                }

                @Override
                public void onRewardAdClosed() {
                    // 激励广告被关闭
                    Log.d("华为激励广告展示成功之后关闭", "onRewardAdClosed");
                    rewardAd.loadAd(new AdParam.Builder().build(), listener);
                    no.errorAndClosed();
                }

                @Override
                public void onRewarded(Reward reward) {
                    // 激励广告奖励达成，发放奖励
                    ok.errorAndClosed();
                    Log.d("华为激励广告展示成功", "onRewarded: 发放奖励");

                }
            });
        }
        return this;
    }
    RewardAdLoadListener listener = new RewardAdLoadListener() {
        @Override
        public void onRewardedLoaded() {
            // 激励广告加载成功
            Log.d("华为激励广告加载成功", "onRewardedLoaded");

        }

        @Override
        public void onRewardAdFailedToLoad(int errorCode) {
            // 激励广告加载失败
            no.errorAndClosed();
            Log.d("华为激励广告加载失败", "onRewardAdFailedToLoad: " + errorCode);
        }
    };
}

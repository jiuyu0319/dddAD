package com.jz.dddad.huawei;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;
import com.jz.dddad.OnAdErrorAndClosed;
import com.jz.dddad.R;

public class HuaweiNativeAd {
    private int type;

    /**
     *     showNativeAd(activity,frameLayout,nativeAd);
     * @param activity
     * @param adId
     * @param type
     * @param nativeAdLoadedListener
     */
    public void loadAd(Activity activity, String adId , int type, NativeAd.NativeAdLoadedListener nativeAdLoadedListener,AdListener adListener) {
        this.type = type;

        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(activity, adId);
        builder.setNativeAdLoadedListener(nativeAdLoadedListener).setAdListener(adListener);

        NativeAdConfiguration adConfiguration = new NativeAdConfiguration.Builder()
                .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
                .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();
        nativeAdLoader.loadAd(new AdParam.Builder().build());

    }

    public void loadAd(Activity activity, FrameLayout frameLayout, String adId , OnAdErrorAndClosed loaded,int type) {
        this.type = type;

        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(activity, adId);
        builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                // Call this method when an ad is successfully loaded.

                // Display native ad.
                if (loaded!= null){
                    loaded.errorAndClosed();
                }
                showNativeAd(activity,frameLayout,nativeAd);
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                // Call this method when an ad fails to be loaded.
            }
        });

        NativeAdConfiguration adConfiguration = new NativeAdConfiguration.Builder()
                .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
                .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();
        nativeAdLoader.loadAd(new AdParam.Builder().build());
    }
    private NativeAd globalNativeAd;

    public void showNativeAd(Activity activity, FrameLayout frameLayout, NativeAd nativeAd) {
        // Destroy the original native ad.
        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }
        globalNativeAd = nativeAd;

        // Obtain NativeView.
        NativeView nativeView;
        if (type == 1){
           nativeView = (NativeView) activity.getLayoutInflater().inflate(R.layout.huawei_native_video_template, null);

        } else {
           nativeView = (NativeView) activity.getLayoutInflater().inflate(R.layout.huawei_native_video_template_small, null);

        }


        // Register and populate a native ad material view.
        initNativeAdView(globalNativeAd, nativeView);
        globalNativeAd.setDislikeAdListener(new DislikeAdListener() {
            @Override
            public void onAdDisliked() {
                // Call this method when an ad is closed.
                frameLayout.removeView(nativeView);
            }
        });

        // Add NativeView to the app UI.
        frameLayout.removeAllViews();
        frameLayout.addView(nativeView);
    }
    private void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        // Register a native ad material view.
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) nativeView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(nativeView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(nativeView.findViewById(R.id.ad_call_to_action));

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }
        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        // Obtain a video controller.
        VideoOperator videoOperator = nativeAd.getVideoOperator();

        // Check whether a native ad contains video materials.
        if (videoOperator.hasVideo()) {
            // Add a video lifecycle event listener.
            videoOperator.setVideoLifecycleListener(videoLifecycleListener);
        }

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);
    }
    private VideoOperator.VideoLifecycleListener videoLifecycleListener = new VideoOperator.VideoLifecycleListener() {
        @Override
        public void onVideoStart() {
        }

        @Override
        public void onVideoPlay() {
        }

        @Override
        public void onVideoEnd() {
            // If there is a video, load a new native ad only after video playback is complete.
        }
    };
}

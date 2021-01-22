package com.jz.dddad.huawei;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;
import com.jz.dddad.R;

/**
 * 如果使用华为ad 需要loadHuaweiAd 后 加载init方法  然后初始化 adlogo  adlogo_des   adlogo_type  adcode  clz;
 *
 * 如果不需要 fl_rootview 传入一个布局 addview可以自定义
 */
public abstract class HuaweiSplashAD extends AppCompatActivity {
    protected FrameLayout fl_root;
    protected RelativeLayout logo;
    protected SplashView splash_ad_view;
    protected int adlogo ;
    protected int adlogo_des ;
    protected int adlogo_type = AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE;
    protected String adcode ;
    protected Class<?> clz; //跳转的页面
    protected Object adimg; // 开屏页下面展示的logo
    protected String ad_des;//开屏页下面展示的名称
    protected String ad_desc;//开屏页下面展示的版本号
    protected int adbac;//广告背景图
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.huaweisplash);
        fl_root = findViewById(R.id.fl_rootview);
        logo = findViewById(R.id.logo);
        splash_ad_view = findViewById(R.id.splash_ad_view);
        init();
        boolean huaweiAd = loadHuaweiAd();
        if (huaweiAd){
            loadAd();
            fl_root.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
            splash_ad_view.setVisibility(View.VISIBLE);
        } else {
            fl_root.setVisibility(View.VISIBLE);
            logo.setVisibility(View.GONE);
            splash_ad_view.setVisibility(View.GONE);
        }

    }
    protected abstract void init();
    protected abstract boolean loadHuaweiAd();

    private static final String TAG = HuaweiSplashAD.class.getSimpleName();

    // 广告展示超时时间：单位毫秒
    private static final int AD_TIMEOUT = 5000;

    // 广告展示超时消息标记
    private static final int MSG_AD_TIMEOUT = 1001;

    /**
     * 暂停标志位。
     * 在开屏广告页面展示时：
     * 按返回键退出应用时需设置为true，以确保应用主界面不被拉起；
     * 从其他页面回到开屏广告页面时需设置为false，以确保仍然可以正常跳转至应用主界面。
     */
    protected boolean hasPaused = false;

    // 广告展示超时消息回调handler
    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (HuaweiSplashAD.this.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });

    private SplashView splashView;

    private SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
        @Override
        public void onAdLoaded() {
            // Call this method when an ad is successfully loaded.
            Log.i(TAG, "SplashAdLoadListener onAdLoaded.");
           // Toast.makeText(HuaweiSplashAD.this, getString(R.string.status_load_ad_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Call this method when an ad fails to be loaded.
            Log.i(TAG, "SplashAdLoadListener onAdFailedToLoad, errorCode: " + errorCode);
           // Toast.makeText(HuaweiSplashAD.this, getString(R.string.status_load_ad_fail) + errorCode, Toast.LENGTH_SHORT).show();
            jump();
        }

        @Override
        public void onAdDismissed() {
            // 广告展示完毕时调用
            Log.i(TAG, "SplashAdLoadListener onAdDismissed.");
           // Toast.makeText(HuaweiSplashAD.this, getString(R.string.status_ad_dismissed), Toast.LENGTH_SHORT).show();
            jump();
        }
    };

    private SplashAdDisplayListener adDisplayListener = new SplashAdDisplayListener() {
        @Override
        public void onAdShowed() {
            // 广告显示时调用
            Log.i(TAG, "SplashAdDisplayListener onAdShowed.");
        }

        @Override
        public void onAdClick() {
            // 广告被点击时调用
            Log.i(TAG, "SplashAdDisplayListener onAdClick.");
        }
    };


    private void loadAd() {
        Log.i(TAG, "Start to load ad");


        ImageView iv_adimg = findViewById(R.id.iv_adimg);
        TextView iv_addes = findViewById(R.id.iv_addes);
        TextView iv_addesc = findViewById(R.id.iv_addesc);
        Glide.with(this)
                .load(adimg)
                .centerCrop()
                .placeholder(adlogo)
                .into(iv_adimg);

        iv_addes.setText(ad_des);
        iv_addesc.setText(ad_desc);
        AdParam adParam = new AdParam.Builder().build();
        splashView = findViewById(R.id.splash_ad_view);
        splashView.setAdDisplayListener(adDisplayListener);
        //设置背景图
        if (adbac>0){
            splashView.setSloganResId(adbac);

        }
        // 设置logo图片
        splashView.setLogoResId(adlogo);
        // 设置logo描述
        splashView.setMediaNameResId(adlogo_des);
        // 设置视频类开屏广告的音频焦点类型
        splashView.setAudioFocusType(adlogo_type);

        splashView.load(adcode, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, adParam, splashAdLoadListener);
        Log.i(TAG, "End to load ad");

        // 移除消息队列中等待的超时消息
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        // 发送延迟消息，用来处理广告展示超时时，能够正常跳转至应用主界面。
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);
    }

    /**
     * 广告展示完毕时，从广告界面跳转至应用主界面
     */

    protected void jump() {
        Log.i(TAG, "jump hasPaused: " + hasPaused);
        if (!hasPaused) {
            hasPaused = true;
            Log.i(TAG, "jump into application");

            startActivity(new Intent(HuaweiSplashAD.this, clz));

            Handler mainHandler = new Handler();
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    /**
     * 按返回键退出应用时需设置为true，以确保应用主界面不被拉起
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "SplashActivity onStop.");
        // 移除消息队列中等待的超时消息
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    /**
     * 从其他页面回到开屏页面时调用，进入应用主界面
     */
    @Override
    protected void onRestart() {
        Log.i(TAG, "SplashActivity onRestart.");
        super.onRestart();
        hasPaused = false;
        jump();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "SplashActivity onDestroy.");
        super.onDestroy();
        if (splashView != null) {
            splashView.destroyView();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "SplashActivity onPause.");
        super.onPause();
        if (splashView != null) {
            splashView.pauseView();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "SplashActivity onResume.");
        super.onResume();
        if (splashView != null) {
            splashView.resumeView();
        }
    }
}

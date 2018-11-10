package com.netease.nim.demo.app;

import android.app.Application;

import com.netease.nim.demo.utils.BaseInfo;
import com.netease.nimlib.sdk.NIMClient;

public class NimDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NIMClient.init(this, null, null);
        BaseInfo.init(this);
    }
}

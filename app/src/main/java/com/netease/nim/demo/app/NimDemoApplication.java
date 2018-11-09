package com.netease.nim.demo.app;

import android.app.Application;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.util.NIMUtil;

public class NimDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NIMClient.init(this, null, null);
        if (NIMUtil.isMainProcess(this)) {
            PinYin.init(this);
            PinYin.validate();
            NimUIKit.init(this);
        }

    }
}

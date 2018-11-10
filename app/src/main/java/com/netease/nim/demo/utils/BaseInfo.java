package com.netease.nim.demo.utils;

import android.content.Context;

public class BaseInfo {

    private static Context sContext;

    private static String sAccount;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }


    public static String getAccount() {
        return sAccount;
    }

    public static void setAccount(String account) {
        sAccount = account;
    }
}

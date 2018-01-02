package com.async.davidconsole.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.async.davidconsole.ui.main.MainActivity;

/**
 * author: Ling Lin
 * created on: 2017/8/2 16:39
 * email: 10525677@qq.com
 * description:
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().endsWith(ACTION_BOOT)) {
            Intent bootIntent = new Intent(context, MainActivity.class);
            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootIntent);
        }
    }
}
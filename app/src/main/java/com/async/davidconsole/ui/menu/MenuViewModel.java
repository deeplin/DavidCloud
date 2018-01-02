package com.async.davidconsole.ui.menu;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 19:13
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class MenuViewModel {

    MenuNavigator menuNavigator;

    @Inject
    public MenuViewModel() {
    }

    public void setMenuNavigator(MenuNavigator menuNavigator) {
        this.menuNavigator = menuNavigator;
    }

    public void clearButtonBorder() {
        if (menuNavigator != null) {
            menuNavigator.clearButtonBorder();
        }
    }

    public void setScreenLock(boolean status) {
        if (menuNavigator != null) {
            menuNavigator.lockScreen(status);
        }
    }
}


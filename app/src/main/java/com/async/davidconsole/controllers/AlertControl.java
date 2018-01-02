package com.async.davidconsole.controllers;

import android.databinding.ObservableField;

import com.async.common.utils.ReflectionUtil;
import com.async.common.utils.ResourceUtil;
import com.async.davidconsole.enums.AlertPriorityMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/7/24 13:11
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class AlertControl {

    private AlertPriorityMode alertPriorityMode = AlertPriorityMode.Sys_None;
    private String alertID = null;
    public ObservableField<String> alertStringField = new ObservableField<>("");

    @Inject
    public AlertControl() {
    }

    /*设置报警*/
    public synchronized void setAlert(AlertPriorityMode alertPriorityMode, String alertID) {
        if (alertPriorityMode.getIndex() < this.alertPriorityMode.getIndex()) {
            if (alertPriorityMode.equals(AlertPriorityMode.Sys_New_Alert)) {
                this.alertPriorityMode = AlertPriorityMode.Sys_Old_Alert;
            } else {
                this.alertPriorityMode = alertPriorityMode;
            }
            this.alertID = alertID;
            alertStringField.set(getAlertDetail(alertID));
        }
    }

    private String getAlertDetail(String alertID) {
        String alertDetail;
        try {
            int resourceID = ReflectionUtil.getResourceId(MainApplication.getInstance().getApplicationContext(),
                    alertID, ReflectionUtil.ResourcesType.string);
            alertDetail = ResourceUtil.getString(resourceID);
        } catch (Exception e) {
            alertDetail = alertID;
        }
        return alertDetail;
    }

    /*是否报警*/
    public boolean isAlert() {
        return !alertPriorityMode.equals(AlertPriorityMode.Sys_None);
    }

    /*是否下位机报警*/
    public String getAlertID() {
        return alertID;
    }

    /*清除所有报警*/
    public synchronized void clearAlert() {
        alertPriorityMode = AlertPriorityMode.Sys_None;
        alertID = null;
        alertStringField.set("");
    }

    /*清除下位机产生报警*/
    void clearRemoteAlert() {
        if (alertID != null) {
            clearAlert();
        }
    }
}

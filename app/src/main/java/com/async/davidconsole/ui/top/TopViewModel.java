package com.async.davidconsole.ui.top;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import com.async.common.ui.IViewModel;
import com.async.common.utils.ReflectionUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.AlertControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ShareMemory;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class TopViewModel implements IViewModel {

    @Inject
    AlertControl alertControl;
    @Inject
    ShareMemory shareMemory;

    public ObservableInt alertVisibility = new ObservableInt(View.INVISIBLE);
    public ObservableField<String> alertMessage = new ObservableField<>();

    public ObservableInt above37Visibility = new ObservableInt(View.INVISIBLE);

    private Observable.OnPropertyChangedCallback vuCallback;

    TopNavigator topNavigator;

    private int previousImageID = 0;
    private int changeImageIndex = 0;
    private boolean batteryAlert = false;

    @Inject
    public TopViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        alertControl.alertStringField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String text = ((ObservableField<String>) observable).get();

                io.reactivex.Observable.just(text)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(string -> {
                            if (string.length() > 0) {
                                alertMessage.set(string);
                                alertVisibility.set(View.VISIBLE);
                            } else {
                                alertVisibility.set(View.INVISIBLE);
                            }

                            String alertId = alertControl.getAlertID();
                            if (alertId != null) {
                                if (alertId.equals("SYS.UPS") || alertId.equals("SYS.BAT")) {
                                    batteryAlert = true;
                                } else {
                                    batteryAlert = false;
                                }
                            } else {
                                batteryAlert = false;
                            }
                        });
            }
        });

        shareMemory.above37.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                if (status) {
                    above37Visibility.set(View.VISIBLE);
                } else {
                    above37Visibility.set(View.INVISIBLE);
                }
            }
        });

        //显示电池电量
        vuCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if(batteryAlert){
                    setBatteryImage(R.mipmap.batteryfault);
                    return;
                }

                int vu = ((ObservableInt) observable).get();
                vu /= 100;

                //todo to be removed
                vu = 96;

                int imageID;
                switch (previousImageID) {
                    case (R.mipmap.battery0):
                        if (vu < 124) {
                            imageID = R.mipmap.battery5;
                        } else {
                            imageID = R.mipmap.battery0;
                        }
                        break;
                    case (R.mipmap.battery1):
                        if (vu > 54) {
                            imageID = R.mipmap.battery2;
                        } else {
                            imageID = R.mipmap.battery1;
                        }
                        break;
                    case (R.mipmap.battery2):
                        if (vu <= 54) {
                            imageID = R.mipmap.battery1;
                        } else if (vu > 69) {
                            imageID = R.mipmap.battery3;
                        } else {
                            imageID = R.mipmap.battery2;
                        }
                        break;
                    case (R.mipmap.battery3):
                        if (vu <= 69) {
                            imageID = R.mipmap.battery2;
                        } else if (vu > 91) {
                            imageID = R.mipmap.battery4;
                        } else {
                            imageID = R.mipmap.battery3;
                        }
                        break;
                    case (R.mipmap.battery4):
                        if (vu <= 91) {
                            imageID = R.mipmap.battery3;
                        } else if (vu > 94) {
                            imageID = R.mipmap.battery5;
                        } else {
                            imageID = R.mipmap.battery4;
                        }
                        break;
                    default:
                        if (vu <= 94) {
                            imageID = R.mipmap.battery4;
                        } else if (vu > 124) {
                            imageID = R.mipmap.battery0;
                        } else {
                            imageID = R.mipmap.battery5;
                        }
                        break;
                }
                if (imageID == R.mipmap.battery4) {
                    changeImageIndex++;
                    if (changeImageIndex > 5) {
                        changeImageIndex = 0;
                    }
                    try {
                        imageID = ReflectionUtil.getResourceId(MainApplication.getInstance().getApplicationContext(),
                                "battery" + changeImageIndex, ReflectionUtil.ResourcesType.mipmap);
                    } catch (Exception e) {
                    }
                    setBatteryImage(imageID);
                    previousImageID = R.mipmap.battery4;
                } else {
                    if (imageID != previousImageID) {
                        setBatteryImage(imageID);
                    }
                    previousImageID = imageID;
                }
            }
        };
    }

    public void setNavigator(TopNavigator topNavigator) {
        this.topNavigator = topNavigator;
    }

    @Override
    public void attach() {
        shareMemory.VU.addOnPropertyChangedCallback(vuCallback);
    }

    @Override
    public void detach() {
        shareMemory.VU.removeOnPropertyChangedCallback(vuCallback);
    }

    private void setBatteryImage(int imageID) {
        topNavigator.setBatteryImage(imageID);
    }
}


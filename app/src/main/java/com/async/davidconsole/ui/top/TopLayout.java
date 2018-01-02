package com.async.davidconsole.ui.top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.async.common.ui.AutoAttachConstraintLayout;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.databinding.LayoutTopBinding;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
public class TopLayout extends AutoAttachConstraintLayout implements TopNavigator{

    @Inject
    TopViewModel topViewModel;
    LayoutTopBinding layoutTopBinding;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutTopBinding = LayoutTopBinding.inflate(layoutInflater, this, true);

        topViewModel.setNavigator(this);
        layoutTopBinding.setViewModel(topViewModel);
    }

    @Override
    public void attach() {
        topViewModel.attach();
    }

    @Override
    public void detach() {
        topViewModel.detach();
    }

    public void setBatteryImage(int imageID) {
        io.reactivex.Observable.just(imageID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Integer ID) -> layoutTopBinding.ivBattery.setImageResource(ID));
    }
}

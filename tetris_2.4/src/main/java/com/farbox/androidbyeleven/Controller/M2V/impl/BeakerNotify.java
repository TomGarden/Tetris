package com.farbox.androidbyeleven.Controller.M2V.impl;

import com.farbox.androidbyeleven.Controller.M2V.IBeakerNotify;
import com.farbox.androidbyeleven.View.Weight.Beaker;

/**
 * describe:来自BeakerModel的通知
 * time: 2017/3/9 10:49
 * email: tom.work@foxmail.com
 */
public class BeakerNotify implements IBeakerNotify {
    private Beaker beaker;

    public BeakerNotify(Beaker beaker) {
        this.beaker = beaker;
    }

    /**
     * BeakerMatris发生变化的通知
     */
    @Override
    public void beakerMatrisChange() {
        this.beaker.invalidate();
    }
}

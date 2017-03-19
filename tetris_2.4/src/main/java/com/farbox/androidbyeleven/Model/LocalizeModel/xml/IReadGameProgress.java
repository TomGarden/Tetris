package com.farbox.androidbyeleven.Model.LocalizeModel.xml;

import com.farbox.androidbyeleven.Model.LocalizeModel.xml.impl.GameProgress;

/**
 * describe:
 * time: 2017/3/15 15:49
 * email: tom.work@foxmail.com
 */
public interface IReadGameProgress {

    /**
     * 获取本地数据
     */
    Object getProgress();

    /**
     * 加载getProgress获取到的数据
     */
    void loadProgress(Object object);
}

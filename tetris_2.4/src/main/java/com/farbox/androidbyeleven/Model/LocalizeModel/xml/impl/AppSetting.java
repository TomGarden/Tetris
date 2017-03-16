package com.farbox.androidbyeleven.Model.LocalizeModel.xml.impl;

import android.content.SharedPreferences;

/**
 * describe: 保存应用配置信息
 * time: 2017/3/15 14:51
 * email: tom.work@foxmail.com
 */
public class AppSetting extends BaseLocalize {

    public final String fileName = "AppSetting";


    public SharedPreferences getSP() {
        return super.getSP(this.fileName);
    }

    public SharedPreferences.Editor getEditor() {
        return super.getEditor(this.fileName);
    }
}

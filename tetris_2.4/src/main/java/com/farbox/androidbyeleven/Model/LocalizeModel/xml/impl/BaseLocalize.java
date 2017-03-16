package com.farbox.androidbyeleven.Model.LocalizeModel.xml.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.farbox.androidbyeleven.Utils.Global;

/**
 * describe:
 * time: 2017/3/15 14:54
 * email: tom.work@foxmail.com
 */
public class BaseLocalize {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferences getSP(String fileName) {
        if (this.sharedPreferences == null) {
            sharedPreferences = Global.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return this.sharedPreferences;
    }

    public SharedPreferences.Editor getEditor(String fileName) {
        if (this.editor == null) {
            this.editor = this.getSP(fileName).edit();
        }
        return editor;
    }
}

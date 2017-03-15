package com.farbox.androidbyeleven.Controller.Control;

import android.content.Context;
import android.content.SharedPreferences;

import com.farbox.androidbyeleven.Model.BaseModel;
import com.farbox.androidbyeleven.Model.HiScore;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.Beaker;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;
import com.farbox.androidbyeleven.View.Weight.TetrisShow;

/**
 * describe:保存游戏进度
 * time: 2017/3/14 9:02
 * email: tom.work@foxmail.com
 */
public class SaveProgress {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public final String fileName = "gameProgress";
    public final String beakerMatrix = "beakerMatrix";
    public final String nextTetrisMatrix = "nextTetrisMatrix";
    public final String currentTetrisMatrix = "currentTetrisMatrix";
    public final String currentTetrisPos = "currentTetrisPos";
    public final String level = "level";
    public final String hiScore = "hiScore";
    public final String defaultStr = "default";

    public SaveProgress() {

    }

    public void saveProgress() {
        if (sharedPreferences == null) {
            sharedPreferences = Global.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
        //保存内容的格式：行数:列数：矩阵内容String
        editor.putString(beakerMatrix, BaseModel.getInstance().getBakerMatris2Str());//20:10:111111111
        editor.putString(nextTetrisMatrix, TetrisShow.getInstance().getMatris2Str());//2:3:1111111111
        editor.putString(currentTetrisMatrix, TetrisMove.getInstance().getMatris2Str());//2:3:1111111111
        editor.putString(currentTetrisPos, TetrisMove.getInstance().getTetrisPos2Str());//1:2
        editor.putString(level, "" + GameThread.getInstance().getLevel());
        editor.putString(hiScore, "" + HiScore.getInstance().getScore());
        editor.apply();
    }

    public SharedPreferences getReader() {
        if (sharedPreferences == null) {
            sharedPreferences = Global.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return this.sharedPreferences;
    }
}

package com.farbox.androidbyeleven.Controller.Control;

import android.content.Context;
import android.content.SharedPreferences;

import com.farbox.androidbyeleven.Model.BaseModel;
import com.farbox.androidbyeleven.Model.HiScore;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
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
    public final String currentTetrisPixPos = "currentTetrisPixPos";
    public final String getCurrentTetrisLogicPos = "getCurrentTetrisLogicPos";
    public final String level = "level";
    public final String hiScore = "hiScore";
    public final String defaultStr = "default";

    public SaveProgress() {

    }

    /**
     * 调用本方法本地化游戏进度。
     * <p>
     * 调用入口包括，手动暂停，息屏暂停
     */
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
        editor.putString(currentTetrisPixPos, TetrisMove.getInstance().getTetrisPixPos2Str());//1:2
        editor.putString(getCurrentTetrisLogicPos, TetrisMove.getInstance().getTetrisLogicPos2Str());//1:2

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

    public void print() {
        this.getReader();
        LogUtil.i(LogUtil.msg() + beakerMatrix + "\t" + sharedPreferences.getString(beakerMatrix, defaultStr));//20:10:111111111
        LogUtil.i(LogUtil.msg() + nextTetrisMatrix + "\t" + sharedPreferences.getString(nextTetrisMatrix, defaultStr));//2:3:1111111111
        LogUtil.i(LogUtil.msg() + currentTetrisMatrix + "\t" + sharedPreferences.getString(currentTetrisMatrix, defaultStr));//2:3:1111111111
        LogUtil.i(LogUtil.msg() + currentTetrisPixPos + "\t" + sharedPreferences.getString(currentTetrisPixPos, defaultStr));//1:2
        LogUtil.i(LogUtil.msg() + getCurrentTetrisLogicPos + "\t" + sharedPreferences.getString(getCurrentTetrisLogicPos, defaultStr));//1:2
        LogUtil.i(LogUtil.msg() + level + "\t" + sharedPreferences.getString(level, defaultStr));
        LogUtil.i(LogUtil.msg() + hiScore + "\t" + sharedPreferences.getString(hiScore, defaultStr));
    }
}

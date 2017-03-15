package com.farbox.androidbyeleven.View.Weight.SuperClass;

import android.content.Context;

import com.farbox.androidbyeleven.Utils.Global;

/**
 * describe:
 * time: 2017/3/7 10:20
 * email: tom.work@foxmail.com
 */
public abstract class TetrisSquare extends MyView {
    public TetrisSquare(Context context) {
        super(context);
    }

    public class Size {
        int width = Global.notSet;
        int height = Global.notSet;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    /**
     * 刷新控件的大小
     */
    protected abstract void refreshSize();

    /**
     * 刷新俄罗斯方块，也就是刷新俄罗斯方块数组和显示的位置
     */
    public abstract void refreshTetris();

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    public abstract String getMatris2Str();
}

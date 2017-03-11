package com.farbox.androidbyeleven.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.androidbyeleven.Util._Log;

/**
 * 杨铭 Created by Tom on 2016/11/7.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 因为两个View 有相同的功能，所以我们抽象出来一个共同的父类
 */

public class MyView extends View {
    //region final
    /**
     * 数字未设置的标志，如果数字是这个量那就是没有设置
     */
    protected final int notSetting = -1;
    //endregion

    /**
     * 容器和俄罗斯方块容器都需要这只画笔来绘制
     */
    private Paint mPaint = null;
    /**
     * 本主题下的哪一个空环的宽度就是用这个属性表示的
     */
    private int ringWidth = notSetting;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取笔触宽度所占的像素数：后文是原理
     * <p>
     * 实际上是获取和strockWith最相近的偶数原理：
     * 2016-09-01[关于Paint笔触的了解]：
     * http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @param strockWith
     * @return >=strockWith 的一个整数
     */
    protected int getStrockWith_Pix(int strockWith) {
        if (isEven(strockWith)) {
            return strockWith;
        } else {
            return strockWith + 1;
        }
    }

    /**
     * 判断num是否为偶数
     *
     * @param num
     * @return true num为偶数，false num不是偶数
     */
    private boolean isEven(int num) {
        if ((num & 1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 绘制单个正方形，与”(终极)绘制单个正方形“相比，这里多一个参数用来求得本类中的 mPaint 和 ringWidth
     *
     * @param canvas
     * @param squareSide_PIX:根据屏幕尺寸和设置的俄罗斯方块物理尺寸得到的每一个格子的像素边长
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected void draw1Square(Canvas canvas, int squareSide_PIX, int left, int top, int right, int bottom) {
        int strokeWidth = squareSide_PIX / 8;
        this.ringWidth = squareSide_PIX / 4;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(strokeWidth);

        draw1Square(canvas, left, top, right, bottom);
    }

    /**
     * (终极)绘制单个正方形
     * <p>
     * 以下参数是有可能会改变的，相对于重载方法，本方法中少的参数，则是相对稳定不变的参数。
     *
     * @param canvas
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @throws NullPointerException 如果抛出异常，说明mPaint或者ringWidth没有合理的初始化应该在catch中调用参数更多的本函数的重载
     */
    protected void draw1Square(Canvas canvas, int left, int top, int right, int bottom) throws NullPointerException {
        //绘制外圈
        canvas.drawRect(left, top, right, bottom, mPaint);
        //绘制内心
        /**外圈和实心的之间的环形距离*/
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }
}

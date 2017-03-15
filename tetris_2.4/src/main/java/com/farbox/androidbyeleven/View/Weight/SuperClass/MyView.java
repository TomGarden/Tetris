package com.farbox.androidbyeleven.View.Weight.SuperClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.androidbyeleven.Utils.Global;

/**
 * Created by Tom on 2016/11/7.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 因为两个View 有相同的功能，所以我们抽象出来一个共同的父类
 */
public class MyView extends View {
    /**
     * 容器和俄罗斯方块容器都需要这只画笔来绘制
     */
    private static Paint mPaint = null;

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
     * [古典]绘制俄罗斯方块
     *
     * @param canvas
     * @param tetrisLineWidthPix
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected final void draw1Square(Canvas canvas, int tetrisLineWidthPix, int left, int top, int right, int bottom) {
        if (this.mPaint == null) {
            this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            this.mPaint.setColor(Color.BLACK);
            this.mPaint.setStyle(Paint.Style.STROKE);
        }
        this.mPaint.setStrokeWidth(tetrisLineWidthPix);
        //绘制外圈
        canvas.drawRect(left, top, right, bottom, mPaint);

        int ringWidth = (int) (tetrisLineWidthPix * 1.5);
        //绘制内心
        /**外圈和实心的之间的环形距离*/
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    protected final void draw1Square(Canvas canvas, int paintColor, int tetrisLineWidthPix, int left, int top, int right, int bottom) {
        if (this.mPaint == null) {
            this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            this.mPaint.setStyle(Paint.Style.STROKE);
        }
        this.mPaint.setColor(paintColor);
        this.mPaint.setStrokeWidth(tetrisLineWidthPix);
        //绘制外圈
        canvas.drawRect(left, top, right, bottom, mPaint);

        int ringWidth = (int) (tetrisLineWidthPix * 1.5);
        //绘制内心
        /**外圈和实心的之间的环形距离*/
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }


}

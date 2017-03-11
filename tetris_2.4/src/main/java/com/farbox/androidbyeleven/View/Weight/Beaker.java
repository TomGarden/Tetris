package com.farbox.androidbyeleven.View.Weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.androidbyeleven.Controller.V2M.IBeakerGetterService;
import com.farbox.androidbyeleven.Controller.V2M.IBeakerConditionService;
import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.SuperClass.MyView;


/**
 * Created by Tom on 2016/9/23. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */
public class Beaker extends MyView {
    /**
     * Braker需求的提供者
     */
    private IBeakerGetterService server = null;
    /**
     * 为了让IBeakerServer满足自己的需求，自己需要给予Server一定的帮助
     */
    private IBeakerConditionService serverCondition = null;
    /**
     * 烧杯背景色
     */
    private int backgroundColor = Global.notSet;
    /**
     * 烧杯背景格子线颜色
     */
    private int paneLineColor = Global.notSet;
    /**
     * 本对象中的基本数据类型只是用一次，使用完成后就把本对象置为空
     */
    private UseOnceData once = new UseOnceData();
    /**
     * 这支画笔用来绘制背景中的格子线
     */
    private Paint mPaint;

    private class UseOnceData {
        /**
         * 烧杯背景中格子线的宽度 ，如果为-1就不绘制格子
         */
        int squareLineWidth = Global.notSet;
        /**
         * 格子的边长[英寸]
         */
        float squareSideInch = Global.notSet;
        /**
         * square间距像素/2
         */
        int halfSquareSpacePix = Global.notSet;
    }

    public Beaker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Beaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Beaker, defStyleAttr, 0);
        this.backgroundColor = typedArray.getColor(R.styleable.Beaker_backgroundColor, getResources().getColor(R.color.transparent));
        this.paneLineColor = typedArray.getColor(R.styleable.Beaker_paneLineColor, Global.notSet);
        this.once.squareLineWidth = typedArray.getInt(R.styleable.Beaker_paneLineWidth, Global.notSet);
        this.once.squareSideInch = typedArray.getFloat(R.styleable.Beaker_squareSideInch, Global.notSet);
        this.once.halfSquareSpacePix = typedArray.getInt(R.styleable.Beaker_halfSquareSpace, Global.notSet);
        typedArray.recycle();
    }

    /**
     * 目前还没能规划好如何设置默认的大小，所以也没有准确的计算法方案，只能设置match
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   //从父布局传过来的内容就是match_parent的数据。
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        //测试过了，这里是最早调用到Beaker尺寸的地方，即使如此这个数据的获取还是比Beaker的属性好晚
        this.serverCondition.setBeakerSize(widthSize, heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.i(LogUtil.msg() + "onDraw(Canvas canvas)");
        if (this.server == null || this.serverCondition == null) {
            throw new RuntimeException(Global.tipNotInitOver);
        }
        this.initMPain();
        //绘制背景
        canvas.drawColor(this.backgroundColor);
        //绘制格子
        int left, top, right, bottom, radius = 5;
        int[][] matris = this.server.getBeakerMatris();
        for (int h = 0; h < matris.length; h++) {
            for (int v = 0; v < matris[0].length; v++) {
                switch (matris[h][v]) {
                    case 0:
                        top = server.getMarginVerticalPix() + server.getSideAddSpacePix() * h + server.getHalfSquareSpacePix() + server.getHalfBeakerBGLineWidthPix();
                        bottom = server.getMarginVerticalPix() + server.getSideAddSpacePix() * (h + 1) - server.getHalfSquareSpacePix() - server.getHalfBeakerBGLineWidthPix();
                        left = server.getMarginHorizontalPix() + server.getSideAddSpacePix() * v + server.getHalfSquareSpacePix() + server.getHalfBeakerBGLineWidthPix();
                        right = server.getMarginHorizontalPix() + server.getSideAddSpacePix() * (v + 1) - server.getHalfSquareSpacePix() - server.getHalfBeakerBGLineWidthPix();
                        if (Build.VERSION.SDK_INT >= 21) {
                            canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
                        } else {
                            RectF rectF = new RectF(left, top, right, bottom);
                            canvas.drawRoundRect(rectF, radius, radius, mPaint);
                        }
                        break;
                    case 1:
                        top = server.getMarginVerticalPix() + server.getSideAddSpacePix() * h + server.getHalfSquareSpacePix() + server.getHalfTetrisLineWidthPix();
                        bottom = server.getMarginVerticalPix() + server.getSideAddSpacePix() * (h + 1) - server.getHalfSquareSpacePix() - server.getHalfTetrisLineWidthPix();
                        left = server.getMarginHorizontalPix() + server.getSideAddSpacePix() * v + server.getHalfSquareSpacePix() + server.getHalfTetrisLineWidthPix();
                        right = server.getMarginHorizontalPix() + server.getSideAddSpacePix() * (v + 1) - server.getHalfSquareSpacePix() - server.getHalfTetrisLineWidthPix();
                        super.draw1Square(canvas, Color.GRAY, server.getTetrisLineWidthPix(), left, top, right, bottom);
                        break;
                    default:
                        throw new RuntimeException("异常请检查");
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 获取自己的服务员。
     *
     * @param waiter 服务员
     */
    public void setService(IBeakerGetterService waiter) {
        this.server = waiter;
    }

    /**
     * 为自己的服务员提供某些必要的帮助
     *
     * @param waiter
     */
    public void setServerCondition(IBeakerConditionService waiter) {
        this.serverCondition = waiter;

        this.serverCondition.setSquareSlideInch(this.once.squareSideInch);
        this.serverCondition.setHalfSquareSpace(this.once.halfSquareSpacePix);
        this.serverCondition.setBeakerBGLineWidthPix(this.once.squareLineWidth);
        this.once = null;
    }

    /**
     * 到使用的时候再初始化画笔
     */
    private void initMPain() {
        if (mPaint != null) {
            return;
        }
        //根据以上获取的属性值设置画笔。
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setStrokeWidth(this.server.getBeakerBGLineWidthPix());
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.paneLineColor);
    }
}



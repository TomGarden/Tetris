package com.farbox.androidbyeleven.mtetris_20.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.farbox.androidbyeleven.mtetris_20.R;

/**
 * 杨铭 Created by Tom on 2016/9/23. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p>
 * 俄罗斯方块烧杯，这里仅仅是烧杯，俄罗斯方块是另一个控件。
 */

public class TetrisBeaker extends View {
    /**
     * 数字未设置的标志，如果数字是这个量那就是没有设置
     */
    private final int notSetting = -1;
    //region 属性
    /**
     * 烧杯背景色
     */
    private int backgroundColor = notSetting;
    /**
     * 烧杯背景格子线颜色
     */
    private int paneLineColor = notSetting;
    /**
     * 格子的宽度单位英寸[1英寸=2.54厘米]
     */
    private float squareSideInch = notSetting;
    /**
     * 烧杯背景中格子线的宽度 ，如果为-1就不绘制格子[既不充分也不必要的条件]
     */
    private int paneLineWidth = notSetting;
    private TetrisAttribute tetrisAttr = TetrisAttribute.getSingleInstance();
    private Paint mPaint;
    /**
     * 水平页边距
     */
    private int marginHorizontal = notSetting;
    /**
     * 垂直页边距
     */
    private int marginVertical = notSetting;

    //endregion
    public TetrisBeaker(Context context) {
        this(context, null);
    }

    public TetrisBeaker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TetrisBeaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.TetrisBeaker,
                        defStyleAttr, 0);
        this.initAttrs(typedArray);
        this.initPaint();
    }

    /**
     * 初始化本空间所需要的所有属性
     * <p/>
     * 从.xml文件中获取各个属性的值,暂时直接用硬编码模拟着这些数据
     */
    private void initAttrs(TypedArray typedArray) {
        this.paneLineColor = typedArray.getColor(R.styleable.TetrisBeaker_paneLineColor, notSetting);
        this.paneLineWidth = typedArray.getInt(R.styleable.TetrisBeaker_paneLineWidth, notSetting);
        this.backgroundColor = typedArray.getColor(R.styleable.TetrisBeaker_backgroundColor, notSetting);
        this.squareSideInch = typedArray.getFloat(R.styleable.TetrisBeaker_squareSideInch, notSetting);
        /**
         * 格子之间距离的一半，看看计算（initTetrisAttribute）就知道了
         */
        int squareSpace = typedArray.getInt(R.styleable.TetrisBeaker_squareSpace, notSetting);
        tetrisAttr.setSquareSpace_PIX(squareSpace);
        typedArray.recycle();
    }

    /**
     * 目前还没能规划好如何设置默认的大小，所以也没有准确的计算法方案，只能设置match
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   //从父布局传过来的内容就是match_parent的数据。
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.initTetrisAttribute();
        this.initMargin();
        canvas.drawColor(this.backgroundColor);
        drawPane(canvas);
    }

    /**
     * @return true，如果事件已经被消耗，false,其他情况。
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            this.mGestureDetector.setLeft(0);
//            this.mGestureDetector.setRight(0);
//            this.mGestureDetector.setBottom(0);
//        }
//        return this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //region init
    private void initPaint() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setStrokeWidth(this.paneLineWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.paneLineColor);
    }

    /**
     * 初始化TetrisAtrribute
     * <p>
     * 需要在一个能够 调用 getWhdth()的地方调用本方法。
     * <p>
     * 以下代码决定了页边距大于等于一半的SquareSide_PIX
     */
    private void initTetrisAttribute() {
        if (this.getWidth() == 0) {
            throw new RuntimeException("initTetrisAttribute调用的时机不对。斟酌重新写吧");
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //得到俄罗斯方块每小块的像素尺寸
        tetrisAttr.setSquareSide_PIX((int) (this.squareSideInch * displayMetrics.densityDpi));
        tetrisAttr.setSideAddSpace(tetrisAttr.getSquareSide_PIX() + tetrisAttr.getSquareSpace_PIX() * 2);
        int horizentalNum = (this.getWidth() - tetrisAttr.getSquareSide_PIX()) / tetrisAttr.getSideAddSpace();
        int verticalNum = (this.getHeight() - tetrisAttr.getSquareSide_PIX()) / tetrisAttr.getSideAddSpace();
        tetrisAttr.setBeakerMatris(new int[verticalNum][horizentalNum]);
    }

    /**
     * 初始化页边距,页边距>=squareSide/2
     */
    private void initMargin() {
        //一个SquareSlide+squareSpace*2
        this.marginHorizontal = (this.getWidth() - tetrisAttr.getSideAddSpace() * tetrisAttr.getBeakerMatris()[0].length) / 2;
        this.marginVertical = (this.getHeight() - tetrisAttr.getSideAddSpace() * tetrisAttr.getBeakerMatris().length) / 2;
    }
    //endregion

    private void drawPane(Canvas canvas) {
        int left, top, right, bottom, radius = 5;
        for (int h = 0; h < this.tetrisAttr.getBeakerMatris().length; h++) {
            for (int v = 0; v < this.tetrisAttr.getBeakerMatris()[0].length; v++) {
                left = this.marginHorizontal + tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX();
                top = this.marginVertical + tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX();
                right = left + tetrisAttr.getSideAddSpace() - tetrisAttr.getSquareSpace_PIX();
                bottom = top + tetrisAttr.getSideAddSpace() - tetrisAttr.getSquareSpace_PIX();

                if (Build.VERSION.SDK_INT >= 21) {
                    canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
                } else {
                    RectF rectF = new RectF(left, top, right, bottom);
                    canvas.drawRoundRect(rectF, 3, 3, mPaint);
                }
            }
        }
    }
}


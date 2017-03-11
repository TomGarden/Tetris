package com.farbox.androidbyeleven.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.View;

import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Util._Log;

/**
 * 杨铭 Created by Tom on 2016/9/23. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p>
 * 俄罗斯方块烧杯，这里仅仅是烧杯，俄罗斯方块是另一个控件。
 */
public class TetrisBeaker extends View {

    //region final
    /**
     * 数字未设置的标志，如果数字是这个量那就是没有设置
     */
    private final int notSetting = -1;
    //endregion

    //region 属性
    public static TetrisBeaker tetrisBeakerInstance = null;
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
    private int squareLineWidth = notSetting;
    /**
     * 方块之间的空隙的一半
     */
    private int squareSpace = notSetting;
    /**
     * 触摸手势
     */
    private GestureDetector mGestureDetector;
    private TetrisAttribute tetrisAttr = null;
    private Paint mPaint;
    private InitOverLinstener initOver;

    //endregion

    //region public TetrisBeaker(){
    public TetrisBeaker(Context context) {
        this(context, null);
    }

    public TetrisBeaker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TetrisBeaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TetrisBeaker, defStyleAttr, 0);
        this.initAttrs(typedArray);
        typedArray.recycle();
        _Log.v(_Log.msg() + "INIT Attrs OVER");
        this.initPaint();
        //this.initGestureDetector();
    }
    //endregion

    //region onMeasure  onDraw  onTouchEvent  onClick   onWindowFocusChanged

    /**
     * 目前还没能规划好如何设置默认的大小，所以也没有准确的计算法方案，只能设置match
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   //从父布局传过来的内容就是match_parent的数据。
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        _Log.i(_Log.msg() + "onMeasure" + String.format("[%d,%d]", widthSize, heightSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.tetrisAttr == null) {
            _Log.e(_Log.msg() + "重新初始化数据了");
            this.tetrisAttr = TetrisAttribute.getSingleInstance();
            this.initTetrisAttribute();
            this.initMargin();
        }
        canvas.drawColor(this.backgroundColor);
        //drawSquare(canvas);
        mDraw(canvas);
        _Log.i(_Log.msg() + "onDraw");
    }

    /**
     * @return true，如果事件已经被消耗，false,其他情况。
     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return this.mGestureDetector.onTouchEvent(event);
//    }

    /**
     * 当本控件绘制完成的时候回调用这个方法。
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.initOver != null) {
            this.initOver.viewInitOver();
            this.initOver = null;
        }
        _Log.i(_Log.msg() + "onWindowFocusChanged");
        _Log.i(_Log.msg() + "onWindowFocusChanged" + String.format("[%d,%d]", getWidth(), getHeight()));
    }

    //endregion

    //region init


    /**
     * 初始化本空间所需要的所有属性
     * <p/>
     * 从.xml文件中获取各个属性的值,暂时直接用硬编码模拟着这些数据
     */
    private void initAttrs(TypedArray typedArray) {
        this.paneLineColor = typedArray.getColor(R.styleable.TetrisBeaker_paneLineColor, notSetting);
        this.squareLineWidth = typedArray.getInt(R.styleable.TetrisBeaker_paneLineWidth, notSetting);
        this.backgroundColor = typedArray.getColor(R.styleable.TetrisBeaker_backgroundColor, getResources().getColor(R.color.transparent));
        this.squareSideInch = typedArray.getFloat(R.styleable.TetrisBeaker_squareSideInch, notSetting);
        /**
         * 格子之间距离的一半，看看计算（initTetrisAttribute）就知道了
         */
        this.squareSpace = typedArray.getInt(R.styleable.TetrisBeaker_squareSpace, notSetting);
    }

    private void initPaint() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setStrokeWidth(this.squareLineWidth);
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
        tetrisAttr.setSquareSpace_PIX(squareSpace);
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
        tetrisAttr.setMarginHorizontal((this.getWidth() - tetrisAttr.getSideAddSpace() * tetrisAttr.getBeakerMatris()[0].length) / 2);
        tetrisAttr.setMarginVertical((this.getHeight() - tetrisAttr.getSideAddSpace() * tetrisAttr.getBeakerMatris().length) / 2);
        _Log.e(_Log.msg() + "初始化背景矩阵");
    }

    /**
     * 初始化触摸手势
     */
    private void initGestureDetector() {
        this.mGestureDetector = new GestureDetector(this.getContext(), new MGestureDetector());
    }

    //endregion

    /**
     * 绘制单独的格子
     */
    private void drawSquare(Canvas canvas) {
        int halfStrockWidth = tetrisAttr.getStrockWith_Pix(this.squareLineWidth) / 2;
        int left, top, right, bottom, radius = 5;
        for (int h = 0; h < this.tetrisAttr.getBeakerMatris().length; h++) {
            top = tetrisAttr.getMarginVertical() + tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + halfStrockWidth;
            bottom = tetrisAttr.getMarginVertical() + tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - halfStrockWidth;
            for (int v = 0; v < this.tetrisAttr.getBeakerMatris()[0].length; v++) {
                left = tetrisAttr.getMarginHorizontal() + tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + halfStrockWidth;
                right = tetrisAttr.getMarginHorizontal() + tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - halfStrockWidth;
                if (this.tetrisAttr.getBeakerMatris()[h][v] == 0) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
                    } else {
                        RectF rectF = new RectF(left, top, right, bottom);
                        canvas.drawRoundRect(rectF, 3, 3, mPaint);
                    }
                } else {
                    TetrisSquare.getMoveTetris().draw1Square(canvas, left, top, right, bottom, TetrisSquare.getMoveTetris().getRingWidth());
                }

            }
        }
        TetrisSquare.printArray(tetrisAttr.getBeakerMatris());
    }

    private void mDraw(Canvas canvas) {
        int left, top, right, bottom, radius = 5;
        int halfStrockWidth_bg = tetrisAttr.getStrockWith_Pix(this.squareLineWidth) / 2;
        for (int h = 0; h < this.tetrisAttr.getBeakerMatris().length; h++) {
            top = tetrisAttr.getMarginVertical() + tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + halfStrockWidth_bg;
            bottom = tetrisAttr.getMarginVertical() + tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - halfStrockWidth_bg;
            for (int v = 0; v < this.tetrisAttr.getBeakerMatris()[0].length; v++) {
                left = tetrisAttr.getMarginHorizontal() + tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + halfStrockWidth_bg;
                right = tetrisAttr.getMarginHorizontal() + tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - halfStrockWidth_bg;
                switch (this.tetrisAttr.getBeakerMatris()[h][v]) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= 21) {
                            canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
                        } else {
                            RectF rectF = new RectF(left, top, right, bottom);
                            canvas.drawRoundRect(rectF, 3, 3, mPaint);
                        }
                        break;
                    case 1:
                        TetrisSquare.getMoveTetris().draw1Square(canvas, left, top, right, bottom, TetrisSquare.getMoveTetris().getRingWidth());
                        break;
                    default:
                        throw new RuntimeException("异常请检查");
                }
            }
        }
    }

//    public void pastTetris() {
//        TetrisSquare moveInstance = TetrisSquare.getMoveTetris();
//        //把tetris粘贴到背景
//        List<Integer> list = this.tetrisAttr.tetrisPast2BeakerMatris(moveInstance.getCurrentMatrixTetris(), moveInstance.getmPointMatrix());
//        if (list == null) {
//            Toast.makeText(getContext(), "Game Over", Toast.LENGTH_LONG).show();
//            return;
//        }
//        //隐藏当前tetris
//        moveInstance.setVisibility(GONE);
//        //消除可以消除的行
//        tetrisAttr.eliminate(list);
//    }

    /**
     * 当初始化完成调用
     */
    public interface InitOverLinstener {
        void viewInitOver();
    }

    public void addInitOverLinstener(InitOverLinstener initOverLinstener) {
        this.initOver = initOverLinstener;
    }

}


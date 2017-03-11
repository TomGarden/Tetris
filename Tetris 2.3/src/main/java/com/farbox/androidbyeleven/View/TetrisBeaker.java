package com.farbox.androidbyeleven.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import com.farbox.androidbyeleven.R;

/**
 * 杨铭 Created by Tom on 2016/9/23. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p>
 * 俄罗斯方块烧杯，这里仅仅是烧杯，俄罗斯方块是另一个控件。
 * <p>
 * 在本控件中android:layout_width与layout_height的值wrap_content是没有处理的等价于match_parent
 * 使用本控件的一个关键是要：(需要修改)
 * <p>
 * 需要修改的位置的关键字“(需要修改)”
 */
public class TetrisBeaker extends MyView {
    //region 属性
    /**
     * 用来获取必要数据的接口
     */
    private IBeakerGetData getData = null;
    /**
     * 烧杯背景色，这个必要添加吗？恐怕不是。(需要修改)
     */
    private int backgroundColor = notSetting;
    /**
     * 烧杯背景格子线颜色
     */
    private int paneLineColor = notSetting;
    /**
     * 烧杯背景中格子线的宽度 ，如果为-1就不绘制格子[既不充分也不必要的条件]
     */
    private int squareLineWidth = notSetting;
    /**
     * 这支画笔用来绘制背景中的格子线
     */
    private Paint mPaint;
    /**
     * 格子的宽度单位英寸[1英寸=2.54厘米]
     */
    private float squareSide_Inch = notSetting;
    /**
     * 方块之间的总空隙，这个是我们设置的总空隙，一切和空隙有关的变量的基准。
     */
    private int squareSpace_Pix = notSetting;
    //region 间接属性
    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     */
    private int squareLintWidth_halfPix = notSetting;

    //endregion
    //endregion

    public TetrisBeaker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TetrisBeaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TetrisBeaker, defStyleAttr, 0);
        this.paneLineColor = typedArray.getColor(R.styleable.TetrisBeaker_paneLineColor, notSetting);
        this.squareLineWidth = typedArray.getInt(R.styleable.TetrisBeaker_paneLineWidth, notSetting);
        this.backgroundColor = typedArray.getColor(R.styleable.TetrisBeaker_backgroundColor, getResources().getColor(R.color.transparent));
        this.squareSide_Inch = typedArray.getFloat(R.styleable.TetrisBeaker_squareSideInch, notSetting);
        this.squareSpace_Pix = typedArray.getInt(R.styleable.TetrisBeaker_squareSpace, notSetting);
        //根据以上获取的属性值设置画笔。
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setStrokeWidth(this.squareLineWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.paneLineColor);
        //根据以上获取的属性值获取间接属性值
        this.squareLintWidth_halfPix = super.getStrockWith_Pix(this.squareLineWidth) / 2;
        //回收就很关键。
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
        getData.initSizeOver();
        //绘制背景
        canvas.drawColor(this.backgroundColor);
        //绘制格子
        int left, top, right, bottom, radius = 5;
        //int squareSpace_halfPix = getData.getSquareSpace_halfPix();
        for (int h = 0; h < this.getData.getBeakerMatris().length; h++) {
            top = getData.getMarginVertical_Pix() + getData.getSideAddSpace_Pix() * h + this.squareLintWidth_halfPix;
            bottom = getData.getMarginVertical_Pix() + getData.getSideAddSpace_Pix() * (h + 1) - squareSpace_Pix - this.squareLintWidth_halfPix;
            for (int v = 0; v < this.getData.getBeakerMatris()[0].length; v++) {
                left = getData.getMarginHorizontal_Pix() + getData.getSideAddSpace_Pix() * v + this.squareLintWidth_halfPix;
                right = getData.getMarginHorizontal_Pix() + getData.getSideAddSpace_Pix() * (v + 1) - squareSpace_Pix - this.squareLintWidth_halfPix;
                switch (this.getData.getBeakerMatris()[h][v]) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= 21) {
                            canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
                        } else {
                            RectF rectF = new RectF(left, top, right, bottom);
                            canvas.drawRoundRect(rectF, 3, 3, mPaint);
                        }
                        break;
                    case 1:
                        try {
                            draw1Square(canvas, left, top, right, bottom);
                        } catch (NullPointerException e) {
                            draw1Square(canvas, getData.getSquareSide_Pix(), left, top, right, bottom);
                        }
                        break;
                    default:
                        throw new RuntimeException("异常请检查");
                }
            }
        }
    }

    public void setDataInterface(IBeakerGetData getData) {
        this.getData = getData;
    }

    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块组成块的边长，单位英寸
     */
    public float getSquareSide_Inch() {
        return squareSide_Inch;
    }

    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块之间的间距，单位像素
     */
    public int getSquareSpace_Pix() {
        return squareSpace_Pix;
    }
}


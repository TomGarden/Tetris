package c508.yangming.teris.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import c508.yangming.teris.Logic.LogicTetris;
import c508.yangming.teris.Logic.LogicTetrisBeaker;
import c508.yangming.teris.Logic.MGestureDetector;
import c508.yangming.teris.R;
import c508.yangming.teris.Util.TomException;
import c508.yangming.teris.Util._Log;

/**
 * 杨铭 Created by kys_8 on 16/8/31,0031.
 * Email:771365380@qq.com
 * Mobile phone:15133350726</p>
 * <p/>
 * 俄罗斯方块烧杯(容器)
 */
public class TetrisBeaker extends View {
    //region 属性
    /**
     * 背景中格子线的颜色 ，如果为-1就不绘制格子[既不充分也不必要的条件]
     */
    private TetrisBeaker tetrisBeaker;
    private int paneLineColor = -1;
    /**
     * 背景中格子线的宽度 ，如果为-1就不绘制格子[既不充分也不必要的条件]
     */
    private float paneLineWidth = -1;

    private Paint mPaint;

    /**
     * 控件宽度
     */
    private float viewWidthPIX = -1;
    /**
     * 控件高度
     */
    private float viewHeightPIX = -1;

    /**
     * 这是我们随便定义的矩形边长，我们默认其为0.1英寸0.25厘米。
     */
    private float paneSideInch = 0.1f;

    /**
     * 逻辑俄罗斯方块
     */
    private LogicTetris logicTetris;

    public LogicTetris getLogicTetris() {
        return logicTetris;
    }

    /**
     * 逻辑俄罗斯方块烧杯
     */
    private LogicTetrisBeaker logicTetrisBeaker = null;

    public LogicTetrisBeaker getLogicTetrisBeaker() {
        return logicTetrisBeaker;
    }

    /**
     * 触摸手势
     */
    private GestureDetector gestureDetector;
    private MGestureDetector mGestureDetector;


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
    }

    /**
     * 初始化本空间所需要的所有属性
     * <p/>
     * 从.xml文件中获取各个属性的值,暂时直接用硬编码模拟着这些数据
     */
    private void initAttrs(TypedArray typedArray) {
        if (this.logicTetris == null) {
            this.logicTetris = new LogicTetris();
            this.logicTetris.createNextTetris();
        }

        this.paneLineColor = typedArray.getColor(R.styleable.TetrisBeaker_paneLineColor, 16711680);
        this.paneLineWidth = typedArray.getDimension(R.styleable.TetrisBeaker_paneLineWidth, -1);
        //region设置画笔
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setStrokeWidth(this.paneLineWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(Color.WHITE);
        //endregion

        this.tetrisBeaker = this;
        this.gestureDetector = new GestureDetector(getContext(),
                this.mGestureDetector = new MGestureDetector(
                        this));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   //从父布局传过来的内容就是match_parent的数据。
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY)//match_parent
        {
            width = widthSize;
        } else {
            //逻辑判断
            width = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //逻辑判断
            height = heightSize;
        }


        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取控件像素
        if (this.viewWidthPIX == -1 || this.viewHeightPIX == -1) {
            this.viewWidthPIX = getWidth();
            this.viewHeightPIX = getHeight();
            _Log.i(_Log.msg() + _Log
                    .likeCoordinate("[宽：高]", this.viewWidthPIX, this.viewHeightPIX));
        }

        this.drawPane(canvas);
    }

    /**
     * 绘制背景 有一个前提，就是我们所绘制的笔触宽度为2
     */
    private void drawPane(Canvas canvas) {
        /*这里我们把默认间隔*/
        int spacePIX = 2;
        /**这个宽度不是没有依据的，根据实验结果知道  private float paneLineWidth = 1; 笔触宽度为2pix，这里还不能从逻辑上得到对应的值*****/
        int paintStrokeWidthPix = 2;

        if (this.logicTetrisBeaker == null)
        //region 初始化和logicTetrisBeaker相关的数据
        {
            this.logicTetrisBeaker = new LogicTetrisBeaker(this);
            //获取DPI
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            _Log.e(_Log.msg() + "Density is " + displayMetrics.density + " densityDpi is " +
                    displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                    " width: " + displayMetrics.widthPixels);
            _Log.e(_Log.msg() + "displayMetrics.xdpi=" + displayMetrics.xdpi + "  displayMetrics" +
                    ".ydpi=" + displayMetrics.ydpi);
            //得到英寸为单位的容器尺寸
            float widthInch = this.viewWidthPIX / (displayMetrics.xdpi * displayMetrics.density);
            float heightInch = this.viewHeightPIX / (displayMetrics.ydpi * displayMetrics.density);
            _Log.e(_Log.msg() + _Log.likeCoordinate("[宽Inch，高Inch]", widthInch, heightInch));
            //像素为单位的格子尺寸***
            this.logicTetrisBeaker
                    .setPaneWidthPix((int) ((this.paneSideInch / widthInch) * this.viewWidthPIX));
            this.logicTetrisBeaker.setPaneHeightPix(
                    (int) ((this.paneSideInch / heightInch) * this.viewHeightPIX));
            _Log.w(_Log.msg() + _Log
                    .likeCoordinate("矩形像素[宽，高]=", this.logicTetrisBeaker.getPaneWidthPix(),
                            this.logicTetrisBeaker.getPaneHeightPix()));
            //求横纵向格子数,为矩阵做准备****
            int numPaneWidth = (int) this.viewWidthPIX / this.logicTetrisBeaker.getPaneWidthPix();
            if (numPaneWidth < 4)
            //region抛出一个异常
            {
                new TomException();
            }
            //endregion
            int numPaneHeight = (int) this.viewHeightPIX / this.logicTetrisBeaker
                    .getPaneHeightPix();
            this.logicTetrisBeaker.setBeakerMatris(new int[numPaneHeight][numPaneWidth]);
            _Log.w(_Log.msg() + _Log.likeCoordinate("矩形个数[宽，高]=", numPaneWidth, numPaneHeight));
            //我们把求得的长宽  理解为  一个格子的长度(考虑笔触)+间隔数
            //现在我们求出绘制区域，当然要减去一个间隔的长度(画个图就明白)
            //得到以像素为单位的绘制区域，这个不用于控件大小的区域，比控件略小
            int drawWidthPIX = this.logicTetrisBeaker.getPaneWidthPix() * numPaneWidth - spacePIX;
            int drawHeightPIX = this.logicTetrisBeaker
                    .getPaneHeightPix() * numPaneHeight - spacePIX;
            _Log.w(_Log.msg() + _Log.likeCoordinate("绘制区域像素[宽，高]=", drawWidthPIX, drawHeightPIX));
            //绘制区域和控件边缘的距离****
            this.logicTetrisBeaker.setPaddingLeft((int) (this.viewWidthPIX - drawWidthPIX) / 2);
            this.logicTetrisBeaker.setPaddingTop((int) (this.viewHeightPIX - drawHeightPIX) / 2);
            _Log.w(_Log.msg() + _Log.likeCoordinate("绘制区域和控件边缘的距离[left，top]=",
                    this.logicTetrisBeaker.getPaddingLeft(),
                    this.logicTetrisBeaker.getPaddingTop()));
        }//endregion

        //当前俄罗斯方块一些信息
        int[][] tetris = this.logicTetris.getCurrentLogicTeris();
        if (this.logicTetrisBeaker.getTetrisColumnIndex() < 0) {
            this.logicTetrisBeaker.setTetrisColumnIndex(
                    this.logicTetrisBeaker.getBeakerMatris()[0].length / 2 - tetris[0].length / 2);
        }
        //当前俄罗斯方块边界
        int tetrisTop = this.logicTetrisBeaker.getTetrisRowIndex() - tetris.length + 1;
        int tetrisBottom = this.logicTetrisBeaker.getTetrisRowIndex();
        int tetrisLeft = this.logicTetrisBeaker.getTetrisColumnIndex();
        int tetrisRight = tetris[0].length + tetrisLeft - 1;


        Paint paint = null;
        //接下来就是绘制了
        for (int h = 0; h < this.logicTetrisBeaker.getBeakerMatris().length; h++) {
            for (int v = 0; v < this.logicTetrisBeaker.getBeakerMatris()[0].length; v++) {
                int left = this.logicTetrisBeaker.getPaddingLeft() + this.logicTetrisBeaker.getPaneWidthPix() * v + paintStrokeWidthPix / 2;
                int top = this.logicTetrisBeaker.getPaddingTop() + this.logicTetrisBeaker.getPaneHeightPix() * h + paintStrokeWidthPix / 2;
                int right = this.logicTetrisBeaker.getPaddingLeft() + this.logicTetrisBeaker.getPaneWidthPix() * (v + 1) - spacePIX - paintStrokeWidthPix / 2;
                int bottom = this.logicTetrisBeaker.getPaddingTop() + this.logicTetrisBeaker.getPaneHeightPix() * (h + 1) - spacePIX - paintStrokeWidthPix / 2;
                int radius = 5;

                //先找出边界来更容易计算
                if (((h <= tetrisBottom && h >= tetrisTop) &&
                        (v >= tetrisLeft && v <= tetrisRight) &&
                        (tetris[h - tetrisTop][v - tetrisLeft] != this.logicTetrisBeaker.getBeakerMatris()[h][v])) ||
                        (this.logicTetrisBeaker.getBeakerMatris()[h][v] == 1)) {
                    paint = this.logicTetris.getPaint();
                } else {
                    if (paint != this.mPaint) {
                        paint = this.mPaint;
                    }
                }

                if (Build.VERSION.SDK_INT >= 21) {
                    canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint);
                } else {
                    RectF rectF = new RectF(left, top, right, bottom);
                    canvas.drawRoundRect(rectF, 3, 3, paint);
                }
            }
        }
    }

    /**
     * @return true，如果事件已经被消耗，false,其他情况。
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.mGestureDetector.setLeft(0);
            this.mGestureDetector.setRight(0);
            this.mGestureDetector.setBottom(0);
        }
        return this.gestureDetector.onTouchEvent(event);
    }
}
package c508.yangming.teris.GameView;

/**
 杨铭 Created by kys_8 on 16/8/30,0030. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import c508.yangming.teris.Model.BlockUnit;
import c508.yangming.teris.Model.TetrisBlock;

/**
 * Created by Terence on 2016/2/5.
 */
public class ShowNextBlockView extends View
{
    /*
     * 用以显示下一个俄罗斯方块
     */

    public TetrisBlock nextBlock = null; //保存下一个俄罗斯方块

    public TetrisBlock createNextBlock()
    {
        /*
         * 创建下一个俄罗斯方块
         */
        nextBlock = new TetrisBlock(TetrisView.beginX, TetrisView.beginPoint);
        return nextBlock;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //画布画笔初始化
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rel;
        float size = BlockUnit.UNITSIZE;

        //方块颜色
        int color[] = {0, Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY};
        if (nextBlock != null)
        {
            //画出下一个俄罗斯方块

            paint.setColor(color[nextBlock.getColor()]);
            for (BlockUnit u : nextBlock.getUnits())
            {

                //设置填充风格
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(color[nextBlock.getColor()]);

                //获取每个方块的横纵坐标
                float tx = (float) (u.getX() - TetrisView.beginX + BlockUnit.UNITSIZE * 1.5);
                float ty = u.getY() + BlockUnit.UNITSIZE;

                //创建圆角矩形
                rel = new RectF(tx, ty, tx + size, ty + size);
                canvas.drawRoundRect(rel, 8, 8, paint);

                //画出边界
                paint.setColor(Color.LTGRAY);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                canvas.drawRoundRect(rel, 8, 8, paint);
            }
        }
    }

    public ShowNextBlockView(Context context)
    {
        /*
         * 构造函数
         */
        super(context);
    }

    public ShowNextBlockView(Context context, AttributeSet attrs)
    {
        /*
         * 构造函数
         */
        super(context, attrs);
    }
}

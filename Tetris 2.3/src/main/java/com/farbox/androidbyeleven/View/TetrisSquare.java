package com.farbox.androidbyeleven.View;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.farbox.androidbyeleven.R;

/**
 * 杨铭 Created by Tom on 2016/10/24.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这是真正的俄罗斯方块，俄罗斯方块由若干Square组成
 */

public class TetrisSquare extends MyView {

    private ISquareGetData getData;

    private TetrisSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                this.getData.getMovingTetrisMatrix()[0].length * getData.getSideAddSpace(),
                this.getData.getMovingTetrisMatrix().length * getData.getSideAddSpace());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.transparent));
        int left, top, right, bottom;
        int strokeWidth_halfPIX = getData.getSquareStrokeWidth_Pix() / 2;
        for (int h = 0; h < getData.getMovingTetrisMatrix().length; h++) {
            top = getData.getSideAddSpace() * h + strokeWidth_halfPIX;
            bottom = getData.getSideAddSpace() * (h + 1) - getData.getSquareSpace_Pix() - strokeWidth_halfPIX;
            for (int v = 0; v < getData.getMovingTetrisMatrix()[0].length; v++) {
                if (getData.getMovingTetrisMatrix()[h][v] == 0) {
                    continue;
                }
                left = getData.getSideAddSpace() * v + strokeWidth_halfPIX;
                right = getData.getSideAddSpace() * (v + 1) - getData.getSquareSpace_Pix() - strokeWidth_halfPIX;
                try {
                    super.draw1Square(canvas, left, top, right, bottom);
                } catch (NullPointerException e) {
                    super.draw1Square(canvas, getData.getSquareSide_Pix(), left, top, right, bottom);
                }
            }
        }
    }
}

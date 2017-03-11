package com.farbox.androidbyeleven.View;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 杨铭 Created by kys_8 on 16/9/8,0008. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p>
 * 实现接口完成手势操作的监听
 * <p>
 * 在实现OnGestureListener接口的类中，如果 ovrride onDown() 方法返回false onScroll不能触发。[还不能知道]
 */
public class MGestureDetector implements GestureDetector.OnGestureListener {
    /**
     * 灵敏度
     */
    private final int sensitivity = 2;
    /**
     * 累计手指滑动的像素数为移动作指导
     */
    private float left = 0, right = 0, bottom = 0, top = 0;

    //region GestureDetector.OnGestureListener
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * 解释：是在touchdown后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
     * 点击一下非常快的（不滑动）Touchup：
     * 　　onDown->onSingleTapUp->onSingleTapConfirmed
     * 点击一下稍微慢点的（不滑动）Touchup：
     * 　　onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
     * <p/>
     * // 短按ACTION_DOWN、ACTION_UP
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        TetrisSquare.getMoveTetris().move(TetrisSquare.Move.rotate);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        if (Math.abs(distanceX / distanceY) > sensitivity) {
//            if (distanceX > 0) {//left
//                this.left += distanceX;
//                if (this.left >= TetrisAttribute.getSingleInstance().getSideAddSpace()) {
//                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.left);
//                    this.left -= TetrisAttribute.getSingleInstance().getSideAddSpace();
//                }
//            } else if (distanceX < 0) {//right
//                this.right -= distanceX;
//                if (this.right >= TetrisAttribute.getSingleInstance().getSideAddSpace()) {
//                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.right);
//                    this.right -= TetrisAttribute.getSingleInstance().getSideAddSpace();
//                }
//            }
//        } else if (Math.abs(distanceY / distanceX) > sensitivity) {
//            if (distanceY > 0) {//top
//                this.top += distanceY;
//                if (this.top >= TetrisAttribute.getSingleInstance().getSideAddSpace()) {
//                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.top);
//                    this.top -= TetrisAttribute.getSingleInstance().getSideAddSpace();
//                }
//            } else if (distanceY < 0) {//down
//                this.bottom -= distanceY;
//                if (this.bottom >= TetrisAttribute.getSingleInstance().getSideAddSpace()) {
//                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.bottom);
//                    this.bottom -= TetrisAttribute.getSingleInstance().getSideAddSpace();
//                }
//            }
//        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 快速滑动
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    //endregion
}
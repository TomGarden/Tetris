package c508.yangming.teris.Model;

/**
 杨铭 Created by kys_8 on 16/8/30,0030. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */

import c508.yangming.teris.GameView.TetrisView;

/**
 * Created by Terence on 2016/2/3.
 */
public class BlockUnit implements Cloneable
{
    /*
     * @class 俄罗斯方块单元块，每个俄罗斯方块包含四个单元块
     *
     */
    public final static float UNITSIZE = 50; //单元块的大小

    public static float max_x, max_y;//单元块的最大横纵坐标

    private float x, y; //单元块的横纵坐标

    public BlockUnit(float x, float y)
    {
        /*
         * @param 单元块横纵坐标
         * 构造函数
         */
        this.x = x;
        this.y = y;
    }

    public boolean canRotate(BlockUnit other)
    {
        /*
         * 判断是否适合进行旋转
         * @param 另一单元块对象
         * @return 若能旋转返回true，否则false
         */

        //超出边界
        if (x < TetrisView.beginPoint / 2 || x >= TetrisView.max_x - UNITSIZE || y >= TetrisView
                .max_y - UNITSIZE)

        {
            return false;
        }
        //与其他单元块重合
        if (Math.abs(x - other.x) <= UNITSIZE / 2 && Math.abs(y - other.y) <= UNITSIZE / 2)
        {
            return false;
        }
        return true;
    }

    public boolean checkOutOfBoundary_Y()
    {
        /*
         * 判断单元块是否在纵向刚好踩到边界，即y坐标是否刚好踩界，
         * 用以判断俄罗斯方块是否能向下移动
         * @return 若超界返回true，否则false
         */
        if (y >= TetrisView.max_y - UNITSIZE * 2)
        {
            return true;
        }
        else if (y - TetrisView.max_y - UNITSIZE * 2 <= 1e-5 && y - TetrisView.max_y - UNITSIZE *
                2 >= -1e-5)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int checkOutOfBoundary_X()
    {
        /*
         * 判断单元块是否在横向刚好踩到边界，即x坐标是否刚好踩界，
         * 用以判断俄罗斯方块是否能左右移动
         * @return 若超界返回true，否则false
         */
        if (x <= 50)
        {
            return -1;
        }
        else if (x >= TetrisView.max_x - UNITSIZE * 2)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public boolean checkVerticalCollision(BlockUnit other)
    {
        /*
         * 判断单元块是否在横向与边界或其他单元块接触
         * @param 另一单元块
         * @return 若接触返回true
         */
        if (y >= TetrisView.max_y - UNITSIZE)
        {
            return true;
        }
        else if (y - TetrisView.max_y - UNITSIZE >= 1e-5 && y - TetrisView.max_y - UNITSIZE <=
                -1e-5)
        {
            return true;
        }
        if (Math.abs(x - other.x) >= UNITSIZE)
        {
            return false;
        }
        else
        {
            if (Math.abs(y - other.y) > UNITSIZE)
            {
                return false;
            }
            else if (y - other.y - UNITSIZE < 1e-5 && y - other.y - UNITSIZE > -1e-5)
            {
                return true;
            }
            return true;
        }
    }

    public int checkHorizontalCollision(BlockUnit other)
    {
        /*
         * 判断单元块是否在纵向与边界或其他单元块接触
         * @param 另一单元块
         * @return 若接触返回true
         */
        if (x <= 50 || x > TetrisView.max_x - UNITSIZE * 2)
        {
            return checkOutOfBoundary_X();
        }
        if (Math.abs(y - other.y) >= UNITSIZE)
        {
            return 0;
        }
        else
        {
            if (Math.abs(x - other.x) > UNITSIZE)
            {
                return 0;
            }
            else if (x - other.x - UNITSIZE <= 1e-5 && x - other.x - UNITSIZE >= -1e-5)
            {
                return -1;
            }
            else if (other.x - x - UNITSIZE <= 1e-5 && other.x - x - UNITSIZE >= -1e-5)
            {
                return 1;
            }

        }
        return 0;
    }

    @Override
    public BlockUnit clone()
    {
        return new BlockUnit(getX(), getY());
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}

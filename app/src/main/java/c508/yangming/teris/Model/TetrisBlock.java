package c508.yangming.teris.Model;

/**
 杨铭 Created by kys_8 on 16/8/30,0030. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */

import java.util.ArrayList;

import c508.yangming.teris.GameView.TetrisView;


/**
 * Created by Terence on 2016/2/3.
 */
public class TetrisBlock implements Cloneable
{
    /*
     * 俄罗斯方块
     */

    public final static int TYPESUM = 7;        //方块种类总数

    public final static int DIRECTIONSUM = 4;   //每个方块有四个方向

    private int blockType, blockDirection;      //方块种类，方块朝向

    private int color;                          //方块颜色

    private float x, y;                         //方块坐标

    private ArrayList<BlockUnit> units = new ArrayList<>();     //方块组成部分

    private ArrayList<TetrisBlock> blocks = new ArrayList<>();  //所有俄罗斯方块

    public void remove(int j)
    {
        /*
         * 删除在第j行上的方块单元
         * @param 需删除行标
         */

        for (int i = units.size() - 1; i >= 0; i--)
        {
            /*
             * ①逆向遍历
             * ②根据y坐标计算单元所在行，若为j行则从units中删除
             */
            if ((int) ((units.get(i).getY() - TetrisView.beginPoint) / 50) == j)
            {
                units.remove(i);
            }
        }
    }

    public boolean canRotate()
    {
        /*
         * 判断方块是否能够翻转
         * @return 若能翻转返回true
         */
        for (TetrisBlock b : blocks)
        {
            //遍历俄罗斯方块所有单元，是否均能翻转，若其中一个单元不能，则俄罗斯方块也不能翻转
            if (canRotate(b) == false)
            {
                return false;
            }
        }
        return true;
    }

    public boolean canRotate(TetrisBlock other)
    {
        /*
         * 判断方块是否能够翻转
         * @return 若能翻转返回true
         * @param 另一俄罗斯方块
         */
        for (BlockUnit i : units)
        {
            //遍历俄罗斯方块所有单元，是否均能翻转，若其中一个单元不能，则俄罗斯方块也不能翻转
            for (BlockUnit j : other.getUnits())
            {
                if (i.canRotate(j) == false)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void move(int x)
    {
        /*
         * 俄罗斯方块左右移动
         */

        // 检查是否接触边界，若接触边界，并且往接触边界移动会超界，故返回
        if (checkCollision_X() < 0 && x < 0 || checkCollision_X() > 0 && x > 0)
        {
            return;
        }

        //更新移动后的坐标
        if (x > 0)
        {
            setX(getX() + BlockUnit.UNITSIZE);
        }
        else
        {
            setX(getX() - BlockUnit.UNITSIZE);
        }
    }

    public boolean checkCollision_Y()
    {
        /*
         * 判断俄罗斯方块是否与其他俄罗斯方块或者边界（底部）接触
         * @return 若接触返回true
         */
        for (BlockUnit u : units)
        {
            //遍历所有单元块判断是否接触底部
            if (u.checkOutOfBoundary_Y())
            {
                return true;
            }
        }
        for (TetrisBlock block : blocks)
        {
            //判断是否与其他俄罗斯方块接触
            if (this == block)
            {
                continue;
            }
            //判断俄罗斯方块底部是否接触其他俄罗斯方块
            if (checkCollision_Y(block))
            {
                return true;
            }
        }
        return false;
    }

    public int checkCollision_X()
    {
        /*
         * 判断俄罗斯方块是否与其他俄罗斯方块或者边界（两侧）接触
         * @return 若接触返回true
         */
        for (BlockUnit u : units)
        {
            //遍历所有单元块判断是否接触两侧
            if (u.checkOutOfBoundary_X() != 0)
            {
                return u.checkOutOfBoundary_X();
            }
        }
        for (TetrisBlock block : blocks)
        {
            if (this == block)
            {
                continue;
            }
            //判断俄罗斯方块两侧是否接触其他俄罗斯方块
            if (checkCollision_X(block) != 0)
            {
                return checkCollision_X(block);
            }
        }
        return 0;
    }

    public boolean checkCollision_Y(TetrisBlock other)
    {
        /*
         * 判断俄罗斯方块是否与其他俄罗斯方块或者边界（底部）接触
         * @return 若接触返回true
         */
        for (BlockUnit i : units)
        {
            //遍历所有单元块判断是否接触底部
            for (BlockUnit j : other.units)
            {
                if (i == j)
                {
                    continue;
                }
                //判断俄罗斯方块底部是否接触其他俄罗斯方块
                if (i.checkVerticalCollision(j))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public int checkCollision_X(TetrisBlock other)
    {
        /*
         * 判断俄罗斯方块是否与其他俄罗斯方块或者边界（两侧）接触
         * @return 若接触返回true
         */
        for (BlockUnit i : units)
        {
            //遍历所有单元块判断是否接触两侧
            for (BlockUnit j : other.units)
            {
                if (i == j)
                {
                    continue;
                }

                //判断俄罗斯方块两侧是否接触其他俄罗斯方块
                if (i.checkHorizontalCollision(j) != 0)
                {
                    return i.checkHorizontalCollision(j);
                }
            }
        }
        return 0;
    }

    public TetrisBlock(float x, float y)
    {
        /*
         * 构造函数
         */
        this.x = x;
        this.y = y;

        blockType = (int) (Math.random() * TYPESUM) + 1;   //随机生成一个种类
        blockDirection = 1;                               //默认初始方向
        color = (int) (Math.random() * 5) + 1;             //随机生成一个颜色

        switch (blockType)
        {
            case 1:
                for (int i = 0; i < 4; i++)
                {
                    units.add(new BlockUnit(x + (-2 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 2:
                units.add(new BlockUnit(x + (-1 + 1) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 3:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE,
                                            y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 4:
                units.add(new BlockUnit(x + (-1 + 0) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 5:
                units.add(new BlockUnit(x + (-1 + 2) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 6:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE,
                                            y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE, y));
                }
                break;
            case 7:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
        }

    }

    public void setX(float x)
    {
        /*
         * 设置俄罗斯方块坐标
         * @param 新坐标值
         */
        float dif_x = x - this.x; //x增量

        for (BlockUnit u : units)
        {
            //根据增量更新方块单元坐标
            u.setX(u.getX() + dif_x);
        }
        this.x = x;
    }

    public void setY(float y)
    {
        /*
         * 设置俄罗斯方块坐标
         * @param 新坐标值
         */

        //若纵坐标超界则返回
        if (checkCollision_Y())
        {
            return;
        }
        float dif_y = y - this.y;//y增量
        for (BlockUnit u : units)
        {
            //根据增量更新方块单元坐标
            u.setY(u.getY() + dif_y);
        }
        this.y = y;

    }

    public TetrisBlock(TetrisBlock other)
    {
        x = other.x;
        y = other.y;
        color = other.color;
        blockDirection = other.blockDirection;
        blockType = other.blockType;
        blocks = other.blocks;
    }

    @Override
    public TetrisBlock clone()
    {

        TetrisBlock block = new TetrisBlock(this);
        for (BlockUnit u : getUnits())
        {
            block.units.add(u.clone());
        }
        return block;
    }

    public ArrayList<TetrisBlock> getBlocks()
    {
        return blocks;
    }

    public void setBlocks(ArrayList<TetrisBlock> blocks)
    {
        this.blocks = blocks;
    }

    public ArrayList<BlockUnit> getUnits()
    {
        return units;
    }

    public void setUnits(ArrayList<BlockUnit> units)
    {
        this.units = units;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public int getBlockDirection()
    {
        return blockDirection;
    }

    public void setBlockDirection(int blockDirection)
    {
        this.blockDirection = blockDirection;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void rotate()
    {
        if (checkCollision_X() != 0 && checkCollision_Y() || blockType == 3)
        {
            return;
        }

        //按顺时针旋转
        for (BlockUnit u : units)
        {
            float tx = u.getX();
            float ty = u.getY();
            u.setX(-(ty - y) + x);
            u.setY(tx - x + y);
        }
    }

}

package c508.yangming.teris.Logic;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import c508.yangming.teris.Util.Global;
import c508.yangming.teris.Util._Log;

/**
 杨铭 Created by kys_8 on 16/9/5,0005. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 <p/>
 逻辑俄罗斯方块，相对视图俄罗斯方块 而言的<p/> 还包括对逻辑俄罗斯方块的处理<p/> 每一个LogicTeris只持有一个逻辑俄罗斯方块
 */
public class LogicTetris
{
    /** 俄罗斯方块矩阵表示 */
    private List<int[][]> matrixList = null;
    /** 当前俄罗斯方块的逻辑矩阵 */
    private int[][] currentLogicTetris = null;

    /** 绘制Tetris的画笔 */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** 创建一个新的俄罗斯方块 */
    public int[][] createNextTetris()
    {
        //region 初始化 matrixList
        if (matrixList == null)
        {
            this.matrixList = new ArrayList<>();
            /** 俄罗斯方块的矩阵表示 */
            int[][] matrixTetris_0 = {{1, 1, 1, 1}};
            int[][] matrixTetris_1 = {{1, 0, 0}, {1, 1, 1}};
            int[][] matrixTetris_2 = {{0, 1, 0}, {1, 1, 1}};
            int[][] matrixTetris_3 = {{0, 0, 1}, {1, 1, 1}};
            int[][] matrixTetris_4 = {{1, 1}, {1, 1}};
            int[][] matrixTetris_5 = {{1, 1, 0}, {0, 1, 1}};
            int[][] matrixTetris_6 = {{0, 1, 1}, {1, 1, 0}};
            matrixList.add(matrixTetris_0);
            matrixList.add(matrixTetris_1);
            matrixList.add(matrixTetris_2);
            matrixList.add(matrixTetris_3);
            matrixList.add(matrixTetris_4);
            matrixList.add(matrixTetris_5);
            matrixList.add(matrixTetris_6);
        }
        //endregion
        this.setTetrisStyle(-1);

        Random random = new Random();
        this.currentLogicTetris = this.getMatrix(random.nextInt(7), random.nextInt(4));


        return this.currentLogicTetris;
    }

    /**
     确认随机出来的矩阵

     @param index     选中的矩阵在集合中的索引
     @param direction 矩阵的方向 0123 上右下左（顺时针）

     @return
     */
    private int[][] getMatrix(int index, int direction)
    {
        int[][] matrix = this.matrixList.get(index);
        //_Log.d(_Log.msg() + "刚创造出来时候");
        Global.localizeLog.write2FileEnter(_Log.msg() + "刚创造出来的俄罗斯方块");
        this.printArray(matrix);

        switch (index)
        {
            case 1:
            case 2:
            case 3:
                break;
            case 0:
            case 5:
            case 6:
                if (direction == 2)
                {
                    direction = 0;
                }
                else if (direction == 3)
                {
                    direction = 1;
                }
                break;
            case 4:
                return matrix;
            default:
                _Log.e(_Log.msg() + "这是一个异常");
        }

        //现在的问题是一个被赋过值的二维数组能不能重新赋值一个结构不同的二维数组？答案是可以的
        for (int i = 0; i < direction; i++)
        {
            matrix = this.rotateMatrix(matrix);
        }
        return matrix;
    }

    /**
     旋转矩阵1次(修改代码可以选择是顺时针还是逆时针旋转)

     @param matrix 已知矩阵

     @return 顺时针旋转后的矩阵
     */
    private int[][] rotateMatrix(int[][] matrix)
    {
        int[][] result = new int[matrix[0].length][matrix.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = 0; newH < matrix[0].length && oldV < matrix[0].length;
             newH++, oldV++)
        {
            for (int newV = 0, oldH = matrix.length - 1; newV < matrix.length && oldH >= 0;
                 newV++, oldH--)
            {
                result[newH][newV] = matrix[oldH][oldV];
            }
        }
       // _Log.e(_Log.msg() + "旋转钱");
        Global.localizeLog.write2FileEnter(_Log.msg() + "旋转前");
        printArray(matrix);
//        _Log.e(_Log.msg() + "旋转后");
        Global.localizeLog.write2FileEnter(_Log.msg() + "旋转后");
        printArray(result);
        return result;
    }

    /** 供外部调用的矩阵旋转 */
    public void rotateMatrix()
    {
        this.currentLogicTetris = rotateMatrix(this.currentLogicTetris);
    }

    /** 获取当前的逻辑矩阵 */
    public int[][] getCurrentLogicTeris()
    {
        return currentLogicTetris;
    }

    /**
     设置俄罗斯方块的风格 ， 目前我们的风格只有颜色
     */
    public void setTetrisStyle(int color)
    {
        this.mPaint.setColor(color);
    }

    public Paint getPaint()
    {
        return this.mPaint;
    }

    /** 用来打印数组做测试的 */
    public void printArray(int[][] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array[0].length; j++)
            {
               // System.out.print(" " + array[i][j]);
                Global.localizeLog.write2File(" " + array[i][j]);
            }
           // System.out.println();
            Global.localizeLog.write2FileEnter();
        }
    }
}

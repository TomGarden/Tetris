package com.farbox.androidbyeleven.Utils;

/**
 * describe:
 * time: 2017/3/6 13:27
 * email: tom.work@foxmail.com
 */
public class MathUtil {
    private MathUtil() {
        LogUtil.e(LogUtil.msg() + "本类不允许创建实例对象。");
    }

    //region matrix转换

    /**
     * 顺时针矩阵旋转[1次]
     */
    public final static int[][] matrixRotateClockwise(int[][] currentMatrix) {
        //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("---旋转之前的", currentMatrix.length, currentMatrix[0].length));
        //Test.printArray(currentMatrix);
        /**转换后的俄罗斯方块矩阵*/
        int[][] result = new int[currentMatrix[0].length][currentMatrix.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = 0; newH < result.length && oldV < currentMatrix[0].length; newH++, oldV++) {//旧列值给新行
            for (int newV = 0, oldH = currentMatrix.length - 1; newV < currentMatrix.length && oldH >= 0; newV++, oldH--) {//旧行值给新列
                result[newH][newV] = currentMatrix[oldH][oldV];
            }
        }
        //this.fixTransform(result);
        //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("+++++旋转之后的", result.length, result[0].length));
        //Test.printArray(result);
        return result;
    }

    /**
     * 逆时针矩阵旋转[1次]
     */
    public final static int[][] matrixRotateAntiClockwise(int[][] currentMatrix) {
        //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("---旋转之前的", currentMatrix.length, currentMatrix[0].length));
        //Test.printArray(currentMatrix);
        int[][] result = new int[currentMatrix[0].length][currentMatrix.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = currentMatrix[0].length - 1; newH < result.length && oldV >= 0; newH++, oldV--) {
            for (int newV = 0, oldH = 0; newV < result[0].length && oldH < currentMatrix.length; newV++, oldH++) {
                result[newH][newV] = currentMatrix[oldH][oldV];
            }
        }
        //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("+++++旋转之后的", result.length, result[0].length));
        //Test.printArray(result);
        return result;
    }
    //endregion
}

package com.farbox.androidbyeleven;

/**
 * describe: 测试工具
 * time: 2017/3/6 14:05
 * email: tom.work@foxmail.com
 */
public class Test {
    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(" " + array[i][j]);
            }
            System.out.println();
        }
    }
}

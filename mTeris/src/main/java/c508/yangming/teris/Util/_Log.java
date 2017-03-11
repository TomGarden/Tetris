package c508.yangming.teris.Util;

import android.util.Log;

/** Created by kys_8 on 2015/12/13. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p> */
public class _Log
{
    /**
     Use for test code.
     */
    public static final String TAG = ">>>>>>YangMing<<<<<<";

    /**
     这个方法将返回调用这个方法的那句代码的位置信息 <p>例: Log.e(MyTag.tag() , "自己的信息");</p> <p>格式【包名.方法名（文件名:行数）】</p>
     <p>see: {@code http://blog.csdn.net/u014587769/article/details/50094435}</p>

     @return 返回String类型的位置信息
     */
    public static String msgDetail()
    {
        StackTraceElement myStackTraceElement = Thread.currentThread().getStackTrace()[3];

        String className = myStackTraceElement.getClassName();
        String methodName = myStackTraceElement.getMethodName();
        String fileName = myStackTraceElement.getFileName();
        int lineNo = myStackTraceElement.getLineNumber();
        //格式【包名.方法名（文件名:行数）】
        return String.format("At %s.%s(%s:%d)>>>", className, methodName, fileName, lineNo);
    }

    public static String msg()
    {
        StackTraceElement myStackTraceElement = Thread.currentThread().getStackTrace()[3];
        String fileName = myStackTraceElement.getFileName();
        int lineNo = myStackTraceElement.getLineNumber();
        //格式【（文件名:行数）】
        return String.format("(%s:%d)>>>", fileName, lineNo);
    }

    /**
     真心记不清了当时包装这个方法的时候好像是有两种方法实现这个功能所以两种方法都记录了下来 <p>2015-12-16 19:54:04测试的时候两个方法 是一样的功能</p>
     <p>暂时先不管先用方法tag()用着先</p>

     @return
     */
    public static String tag_1()
    {
        StackTraceElement myStackTraceElement = new Throwable().getStackTrace()[1];

        String className = myStackTraceElement.getClassName();
        String fileName = myStackTraceElement.getFileName();
        String methodName = myStackTraceElement.getMethodName();
        int lineNo = myStackTraceElement.getLineNumber();
        //格式【包名.方法名（文件名:行数）】
        return String.format("At %s.%s(%s:%d)", className, methodName, fileName, lineNo);
    }

    /**
     综合所有的同名方法或可代替也不一定

     @return
     */
    public static String likeCoordinate(String describe, Object num1, Object num2)
    {
        String string = String.format("%s:[%S,%S]", describe, num1.toString(), num2.toString());
        return string;
    }

    public static String likeCoordinate(String describe, Object num1)
    {
        String string = String.format("%s:[%S]", describe, num1.toString());
        return string;
    }

    public static void v(String msg)
    {
        Log.v(_Log.TAG, msg);
    }

    public static void d(String msg)
    {
        Log.d(_Log.TAG, msg);
    }

    public static void i(String msg)
    {
        Log.i(_Log.TAG, msg);
    }

    public static void w(String msg)
    {
        Log.w(_Log.TAG, msg);
    }

    public static void e(String msg)
    {
        Log.e(_Log.TAG, msg);
    }


}

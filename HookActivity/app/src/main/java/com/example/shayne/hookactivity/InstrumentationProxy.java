package com.example.shayne.hookactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by shayne on 17-6-5.
 */

public class InstrumentationProxy extends Instrumentation {
    public static final String TAG = "InstrumentationProxy";
    public static final String EXEC_START_ACTIVITY = "execStartActivity";

    private static boolean flag = false;
    private static Context g_who = null;
    private static IBinder g_contextThread = null;
    private static IBinder g_token = null;
    private static Activity g_target = null;
    private static Intent g_intent = null;
    private static int g_requestCode = -1;
    private static Bundle g_options = null;

    // ActivityThread里面原始的Instrumentation对象,这里千万不能写成mInstrumentation,这样写
    //抛出异常，已亲测试，所以这个地方就要注意了
    public Instrumentation oldInstrumentation;

    private void showNormalDialog(Context context){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("您的APK已经被HOOK掉了");
        normalDialog.setMessage("是否已经联系Shayne");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = true;

                        execStartActivity(g_who, g_contextThread, g_token, g_target,
                                g_intent, g_requestCode, g_options);

                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    //通过构造函数来传递对象
    public InstrumentationProxy(Instrumentation mInstrumentation) {
        oldInstrumentation = mInstrumentation;
    }


    //这个方法是由于原始方法里面的Instrumentation有execStartActivity方法来定的
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {
        Log.d(TAG, "\n打印调用startActivity相关参数: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");
        g_who = who;
        g_contextThread = contextThread;
        g_token = token;
        g_target = target;
        g_intent = intent;
        g_requestCode = requestCode;
        g_options = options;

        Log.i(TAG, "------------hook  success------------->");
        Log.i(TAG, "这里可以做你在打开StartActivity方法之前的事情");
        Log.i(TAG, "------------hook  success------------->");
        Log.i(TAG, "who.toString().contains(\"PreStartActivity\") = " + who.toString().contains("PreStartActivity"));
        Log.i(TAG, "");

        if(who.toString().contains("PreStartActivity") ||
                who.toString().contains("StartPublicActivity") || flag){
            //由于这个方法是隐藏的，所以需要反射来调用，先找到这方法
            try {
                Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                        EXEC_START_ACTIVITY,
                        Context.class, IBinder.class, IBinder.class, Activity.class,
                        Intent.class, int.class, Bundle.class);
                execStartActivity.setAccessible(true);
                return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                        contextThread, token, target, intent, requestCode, options);
            } catch (Exception e) {
                //如果你在这个类的成员变量Instrumentation的实例写错mInstrument,代码讲会执行到这里来
                throw new RuntimeException("if Instrumentation paramerter is mInstrumentation, hook will fail");
            }
        }else {
            showNormalDialog(who);
            return null;
        }
    }

}

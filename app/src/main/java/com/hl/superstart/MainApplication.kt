package com.hl.superstart;

import android.app.Application
import android.content.Context
import android.content.Intent
import com.hl.superstart.mvp.view.MainActivity

class MainApplication : Application(), Thread.UncaughtExceptionHandler{
    private var instance:Application?=null;

    override fun onCreate() {
        super.onCreate();
        instance = this;

        ///< 设置Thread Exception Handler
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 获取App上下文
     * @return
     */
    fun getInstance(): Application? {
        return instance;
    }

    fun Any.attachBaseContext(base: Any) {
        super.attachBaseContext(base as Context?);
    }

    override fun onTerminate() {
        super.onTerminate();
    }

    /**
     * 异常重启 - HomeActivity里面做了fragment处理
     *
     * @param thread
     * @param throwable
     */
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        System.exit(0);
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ///< 获取intent对象
        intent.setClass(this, MainActivity::class.java)
        ///< 获取class是使用::反射
        startActivity(intent)
    }
}

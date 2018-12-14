package com.hl.superstart.tools

import android.os.PowerManager
import android.view.WindowManager
import android.app.Activity
import android.app.Service
import android.content.Context

/*
*@Description: 屏幕常量工具
*@Author: hl
*@Time: 2018/12/14 11:42
*/
class LongLightUtils {
    /**
     * 静态函数
     */
    companion object {
        /**
         * 是否使屏幕常亮
         *
         * @param activity
         */
        fun keepScreenLongLight(activity: Activity) {
            val isOpenLight = CommSharedUtil.getInstance(activity).getBoolean(CommSharedUtil.FLAG_IS_OPEN_LONG_LIGHT, true)
            if (isOpenLight) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        /**
         * 清除屏幕常量
         */
        fun dontkeepScreenLongLight(activity: Activity) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        /**
         * 打开休眠锁只能保持手机不休眠
         * @param context
         */
        @Deprecated("")
        fun openWakeLock(context: Context) {
            val powerManager = context.getSystemService(Service.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock")
            //是否需计算锁的数量
            wakeLock.setReferenceCounted(false)
            //请求常亮，onResume()
            wakeLock.acquire()
        }

        /**
         * 关闭休眠锁
         * @param context
         */
        @Deprecated("")
        fun closeWakeLock(context: Context) {
            val powerManager = context.getSystemService(Service.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock")
            //是否需计算锁的数量
            wakeLock.setReferenceCounted(false)
            //取消屏幕常亮，onPause()
            wakeLock.release()
        }
    }
}
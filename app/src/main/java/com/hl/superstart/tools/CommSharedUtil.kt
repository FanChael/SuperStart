package com.hl.superstart.tools

import android.content.Context
import android.content.SharedPreferences

/*
*@Description: SharedUtil工具类
*@Author: hl
*@Time: 2018/12/14 11:42
*/
class CommSharedUtil private constructor(context: Context){
    private val SHARED_PATH = "app_info"
    private var sharedPreferences: SharedPreferences? = null

    /**
     * 构造函数
     */
    init{
        sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE)
    }

    /**
     * 静态函数变量 + 单例获取实例
     */
    companion object {
        @Volatile
        var instance: CommSharedUtil ? = null
        val FLAG_IS_OPEN_LONG_LIGHT = "longlight"

        fun getInstance(context: Context): CommSharedUtil {
            if (instance == null) {
                synchronized(CommSharedUtil ::class) {
                    if (instance == null) {
                        instance = CommSharedUtil (context)
                    }
                }
            }
            return instance!!
        }
    }

    fun putInt(key: String, value: Int) {
        val edit = sharedPreferences!!.edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun getInt(key: String): Int {
        return sharedPreferences!!.getInt(key, 0)
    }


    fun putString(key: String, value: String) {
        val edit = sharedPreferences!!.edit()
        edit.putString(key, value)
        edit.apply()
    }


    fun getString(key: String): String? {
        return sharedPreferences!!.getString(key, null)
    }


    fun putBoolean(key: String, value: Boolean) {
        val edit = sharedPreferences!!.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }


    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences!!.getInt(key, defValue)
    }

    fun remove(key: String) {
        val edit = sharedPreferences!!.edit()
        edit.remove(key)
        edit.apply()
    }
}
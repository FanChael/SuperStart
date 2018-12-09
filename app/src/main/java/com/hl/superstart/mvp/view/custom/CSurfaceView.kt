package com.hl.superstart.mvp.view.custom

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import android.util.DisplayMetrics





/**
 * 自定义SurfaceView
 */
class CSurfaceView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) :
        SurfaceView(context, attributeSet, defaultStyle), SurfaceHolder.Callback, Runnable {
    private var bDrawing = false
    private var mWidth : Int? = 0
    private var mHeight : Int? = 0
    private var startX : Float? = 0f
    private var startY : Float? = 0f
    private var mPaint: Paint? = null
    private var mTPaint: Paint? = null
    private var message:String? ="请输入显示文本"
    private var rect = Rect()

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null, 0)

    /**
     * 初始化
     */
    init {
        ///< 初次获取屏幕信息
        var dm:DisplayMetrics = context.getResources().getDisplayMetrics()
        mWidth = dm.widthPixels
        mHeight = dm.heightPixels

        ///< surfaceview持有器回调监听
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)

        ///< 背景画笔
        mPaint = Paint()
        mPaint!!.setColor(Color.parseColor("#ff4ab1"))
        mPaint!!.setStrokeWidth(10f)

        ///< 文字画笔
        mTPaint = Paint()
        mTPaint!!.setTextSize(150f)
        mTPaint!!.setStyle(Paint.Style.STROKE);
        mTPaint!!.setColor(Color.parseColor("#000000"))
        mTPaint!!.setStrokeWidth(4f)
        mTPaint!!.getTextBounds(message, 0, message!!.length, rect);

        ///< 计算文本初始显示位置
        startX = 0f
        startY = (mHeight!! /2 - (rect.bottom - rect.top)/2).toFloat()
    }

    /**
     * 屏幕旋转
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        var dm:DisplayMetrics = context.getResources().getDisplayMetrics()
        mWidth = dm.widthPixels
        mHeight = dm.heightPixels

        startX = 0f
        startY = (mHeight!! /2 - (rect.bottom - rect.top)/2).toFloat()
    }

    /**
     * 画布改变处理（如果是全屏画布，可以这里获取宽高)
     */
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        //mWidth = width
        //mHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        ///< 停止绘制
        bDrawing = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        ///< 开启绘制
        bDrawing = true
        Thread(this).start()
    }

    override fun run() {
        ///< 不停的绘制
        while (bDrawing) {
            ///< 绘制刷新处理
            drawingSomething()
            try {
                ///< 控制刷新频率
                Thread.sleep(200)
            } catch (e: InterruptedException) {
            }
        }
    }

    /**
     * 绘制点东东
     */
    private fun drawingSomething() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (null != canvas) {
                synchronized(holder) {
                    print("test drawingSomething")
                    ///< 清空画布-透明处理
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    ///< 绘制点东东
                    canvas.drawRect(Rect(0, 0, width, height), mPaint)
                    canvas.drawText(message, startX!!, startY!!, mTPaint)
                }
            }
        } finally {
            if (null != canvas) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    /**
     * 设置显示文字
     */
    public fun setMessage(msg : String){
        message = msg;
        mTPaint!!.getTextBounds(msg, 0, msg.length, rect);

        startX = 0f
        startY = (mHeight!! /2 - (rect.bottom - rect.top)/2).toFloat()
    }
}
package com.hl.superstart.mvp.view.custom

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Toast
import android.R.attr.y
import android.R.attr.x




/*
*@Description: 自定义SurfaceView
*@Author: hl
*@Time: 2018/12/10 10:02
*/
class CSurfaceView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) :
        SurfaceView(context, attributeSet, defaultStyle), SurfaceHolder.Callback, Runnable {
    /**
     * 文本绘制相关位置，大小，滚动速度，是否循环等
     */
    private var mWidth: Int? = 0
    private var mHeight: Int? = 0
    private var startX: Float? = 0f
    private var startY: Float? = 0f
    private var sclloStep: Float? = 6f
    private var bGo: Boolean = true
    private var bMove: Boolean = true

    /**
     * 手势相关
     */
    private var oldDist: Double? = 0.0
    private var count = 0;                    ///< 点击次数
    private var firstClick:Long = 0;         ///< 第一次点击时间
    private var secondClick:Long = 0;        ///< 第二次点击时间
    private var totalTime:Long = 1000;      ///< 两次点击时间间隔，单位毫秒

    /**
     * 画笔相关
     */
    private var bDrawing = false
    private var mPaint: Paint? = null
    private var mTPaint: Paint? = null
    private var message: String? = "吴怡萱(you)，我爱你"
    private var rect = Rect()
    private var textSize: Float? = 100.0f
    private var textSizeStep: Float? = 6f
    private val textColorsArray: Array<String> = arrayOf("#000000", "#ec330e", "#230eec", "#0eec19", "#ec0e79", "#f3db0b")

    /**
     * 显示模式
     * 1. 循环
     * 2. 回弹
     */
    enum class DISPLAY_TYPE {
        LOOP, BOUNCE
    }

    private var disType: DISPLAY_TYPE? = DISPLAY_TYPE.BOUNCE

    /**
     * 手势类型
     */
    enum class HANDLE_TYPE {
        NONE, ZOOM
    }

    private var handleType: HANDLE_TYPE? = HANDLE_TYPE.NONE

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null, 0)

    /**
     * 初始化
     */
    init {
        ///< 初次获取屏幕信息
        var dm: DisplayMetrics = context.getResources().getDisplayMetrics()
        mWidth = dm.widthPixels
        mHeight = dm.heightPixels

        ///< surfaceview持有器回调监听
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)
        ///< 解决上层控件被遮挡的问题
        setZOrderMediaOverlay(true)

        ///< 背景画笔
        mPaint = Paint()
        mPaint!!.setColor(Color.parseColor("#ffffffff"))
        mPaint!!.setStrokeWidth(10f)

        ///< 文字画笔
        mTPaint = Paint()
        mTPaint!!.setTextSize(textSize!!)
        mTPaint!!.setStyle(Paint.Style.STROKE);
        mTPaint!!.setColor(Color.parseColor(textColorsArray[0]))
        mTPaint!!.setStrokeWidth(4f)
        mTPaint!!.getTextBounds(message, 0, message!!.length, rect);

        ///< 计算文本初始显示位置
        startX = 0f
        startY = (mHeight!! / 2 - (rect.bottom - rect.top) / 2).toFloat()
    }

    /**
     * 屏幕旋转
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        var dm: DisplayMetrics = context.getResources().getDisplayMetrics()
        mWidth = dm.widthPixels
        mHeight = dm.heightPixels

        startX = 0f
        startY = (mHeight!! / 2 - (rect.bottom - rect.top) / 2).toFloat()
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
                Thread.sleep(10)
            } catch (e: InterruptedException) {
            }
        }
    }

    /**
     * 手势处理
     * 1.双指缩放
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        ///< 手指数量
        when (event!!.getActionMasked()) {
            MotionEvent.ACTION_DOWN -> {
                /**
                 * 双击手势处理
                 */
                count++
                if (1 == count) {
                    firstClick = System.currentTimeMillis()       ///< 记录第一次点击时间
                } else if (2 == count) {
                    secondClick = System.currentTimeMillis()      ///< 记录第二次点击时间
                    if ((secondClick - firstClick) < totalTime) { ///< 判断二次点击时间间隔是否在设定的间隔时间之内
                        ///< 双击
                        bMove = !bMove
                        count = 0
                        firstClick = 0
                    } else {
                        firstClick = secondClick
                        count = 1
                    }
                    secondClick = 0
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                /**
                 * 双指缩放
                 */
                oldDist = spacing(event)
                if (oldDist!! > 10f) {
                    handleType = HANDLE_TYPE.ZOOM;
                }
            }
            MotionEvent.ACTION_MOVE -> {
                /**
                 * 双指缩放 - 模拟器鼠标右键滑动可以调试
                 */
                if (handleType == HANDLE_TYPE.ZOOM) {
                    val newDist: Double = spacing(event)
                    if (newDist > 10f) {
                        val scale: Double = newDist / oldDist!!
                        if (scale > 1.0){
                            scaleSize(1f)
                        }else if (scale < 1.0){
                            scaleSize(-1f)
                        }
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                handleType = HANDLE_TYPE.NONE;
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
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
                    ///< 清空画布-透明处理
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    ///< 绘制背景
                    canvas.drawRect(Rect(0, 0, width, height), mPaint)

                    ///< 移动控制
                    if (bMove) {
                        ///< 绘制文字
                        if (disType == DISPLAY_TYPE.LOOP) {
                            startX = startX!! + sclloStep!!
                            if (startX!! >= width) {
                                startX = -(rect.right - rect.left).toFloat()
                            }
                        } else if (disType == DISPLAY_TYPE.BOUNCE) {
                            if (startX!! <= 0) {
                                bGo = true;
                            }
                            if ((startX!! + (rect.right - rect.left).toFloat()) >= width) {
                                bGo = false;
                            }
                            if (bGo) {
                                startX = startX!! + sclloStep!!
                            } else {
                                startX = startX!! - sclloStep!!
                            }
                        }
                    }

                    ///< 颜色控制
                    mTPaint!!.setColor(Color.parseColor(textColorsArray[(Math.random() * 6 + 0).toInt()]))

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
     * 求两点间的距离
     */
    private fun spacing(event: MotionEvent): Double {
        try {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            return Math.sqrt((x * x + y * y).toDouble())
        } catch (e: IllegalArgumentException) {
        }
        return 1.0;
    }

    /**
     * 设置显示文字
     */
    public fun setMessage(msg: String) {
        message = msg;
        mTPaint!!.getTextBounds(msg, 0, msg.length, rect);

        startX = 0f
        startY = (mHeight!! / 2 - (rect.bottom - rect.top) / 2).toFloat()
    }

    /**
     * 设置文字大小
     */
    public fun setSize(size: Float) {
        mTPaint!!.setTextSize(size)
        mTPaint!!.getTextBounds(message, 0, message!!.length, rect);

        startX = 0f
        startY = (mHeight!! / 2 - (rect.bottom - rect.top) / 2).toFloat()
    }

    /**
     * 字符缩放
     */
    public fun scaleSize(scale: Float) {
        if ((textSize!! + textSizeStep!! * scale) > 260) return
        if ((textSize!! + textSizeStep!! * scale) < 60) return
        textSize = textSize!! + textSizeStep!! * scale
        setSize(textSize!!)
    }

    /**
     * 设置文字大小
     */
    public fun setMode(mode: DISPLAY_TYPE) {
        disType = mode
    }

    /**
     * 获取模式
     */
    public fun getMode() : DISPLAY_TYPE? {
        return disType
    }
}
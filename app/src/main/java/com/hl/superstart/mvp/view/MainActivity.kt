package com.hl.superstart.mvp.view

import android.content.res.Configuration
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.WindowManager
import com.hl.superstart.R
import com.hl.superstart.mvp.view.fragment.HomeFragment
import com.hl.superstart.mvp.view.fragment.OnFragmentInteractionListener
import com.hl.superstart.tools.LongLightUtils
import com.hl.superstart.tools.SensorManagerHelper

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private var homePage: HomeFragment ?=null;
    private var portrait:Boolean ?= false;
    private var sensorHelper: SensorManagerHelper ?=null
    private var bHidden: Boolean ?= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        //sample_text.cssp_text = stringFromJNI()
        initView();
        initService();
    }

    /**
     * 界面初始化
     */
    private fun initView() {
        if (null == homePage)
            homePage = HomeFragment.newInstance("", "")
        val transaction = supportFragmentManager.beginTransaction();
        if (!homePage!!.isAdded()) {
            transaction.add(R.id.am_pageContentFl, homePage!!)
        } else {
            if (homePage!!.isHidden())
                transaction.show(homePage!!)
        }
        transaction.commit()
    }

    private fun initService() {
        ///< 屏幕常量
        LongLightUtils.keepScreenLongLight(this)

        ///< 摇一摇
        sensorHelper = SensorManagerHelper(this)
        sensorHelper!!.setOnShakeListener(object : SensorManagerHelper.OnShakeListener{
            override fun onShake() {
                bHidden = !bHidden!!
                homePage!!.HideOrShow(bHidden)
            }
        })
    }

    /**
     * 横竖屏切换
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        portrait = (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        tryFullScreen(!portrait!!)
    }

    /**
     * 全屏方式-隐藏标题栏
     */
    fun tryFullScreen(fullScreen:Boolean) {
        var supportActionBar: ActionBar? = getSupportActionBar()
        ///< 有Bar就隐藏bar
        if (supportActionBar != null) {
            if (fullScreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
        }
        setFullScreen(fullScreen);
    }

    private fun setFullScreen(fullScreen:Boolean) {
        var attrs: WindowManager.LayoutParams  = getWindow().getAttributes()
        if (fullScreen) {
            attrs.flags = attrs.flags.or(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            getWindow().setAttributes(attrs)
        } else {
            ///< 取消全屏的方式a
            //attrs.flags = attrs.flags.and(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            //getWindow().setAttributes(attrs);
            ///< 取消全屏的方式b
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        sensorHelper!!.start()
    }

    override fun onPause() {
        super.onPause()
        sensorHelper!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        LongLightUtils.dontkeepScreenLongLight(this)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}

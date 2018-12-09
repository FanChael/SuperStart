package com.hl.superstart.mvp.view

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hl.superstart.R
import com.hl.superstart.mvp.view.Fragment.HomeFragment
import com.hl.superstart.mvp.view.Fragment.OnFragmentInteractionListener

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    var homePage: HomeFragment ?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        //sample_text.text = stringFromJNI()
        initView();
    }

    /**
     * 界面初始化
     */
    private fun initView() {
        if (null == homePage)
            homePage = HomeFragment.newInstance("", "");
        val transaction = supportFragmentManager.beginTransaction();
        if (!homePage!!.isAdded()) {
            transaction.add(R.id.am_pageContentFl, homePage!!);
        } else {
            if (homePage!!.isHidden())
                transaction.show(homePage!!);
        }
        transaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

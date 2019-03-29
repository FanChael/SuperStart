package com.hl.superstart.mvp.view.fragment

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.hl.superstart.R
import com.hl.superstart.mvp.view.MainActivity
import com.hl.superstart.mvp.view.custom.CSurfaceView
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fh_textIv.setOnClickListener(this)
        fh_modelIv.setOnClickListener(this)
        fh_oretationIv.setOnClickListener(this)
        fh_btmMessageOkBtn.setOnClickListener(this)
    }

    /**
     * 碎片内部点击事件
     */
    override fun onClick(v: View?) {
        when (v!!.id){
            ///< 显示/隐藏输入框
            R.id.fh_textIv -> {
                if (fh_bottomMessageEditor.visibility == View.INVISIBLE){
                    fh_bottomMessageEditor.visibility = View.VISIBLE
                }else{
                    fh_bottomMessageEditor.visibility = View.INVISIBLE
                }
            }
            ///< 设置显示文本
            R.id.fh_btmMessageOkBtn -> {
                if (fh_btmMessageEt.text.toString().trim().equals("")){
                    Toast.makeText(context, "请输入显示文本", Toast.LENGTH_SHORT).show()
                    return
                }
                fh_startShowSv.setMessage(fh_btmMessageEt.text.toString())
                fh_bottomMessageEditor.visibility = View.INVISIBLE
            }
            ///< 滚动模式选择
            R.id.fh_modelIv -> {
                if (fh_startShowSv.getMode() == CSurfaceView.DISPLAY_TYPE.LOOP){
                    fh_modelIv.setImageResource(R.drawable.bounce)
                    fh_startShowSv.setMode(CSurfaceView.DISPLAY_TYPE.BOUNCE)
                }else{
                    fh_modelIv.setImageResource(R.drawable.loop)
                    fh_startShowSv.setMode(CSurfaceView.DISPLAY_TYPE.LOOP)
                }
            }
            ///< 屏幕旋转
            R.id.fh_oretationIv -> {
                if (context is MainActivity){
                    ///< 判断当前屏幕方向
                    if ((context as MainActivity).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        ///< 切换竖屏
                        (context as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        ///< 切换横屏
                        (context as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                }
            }
        }
    }

    fun HideOrShow(bShow: Boolean?) {
        if (!bShow!!){
            fh_textIv.visibility = View.INVISIBLE
            fh_modelIv.visibility = View.INVISIBLE
            fh_bottomMessageEditor.visibility = View.INVISIBLE
            fh_zoomIv.visibility = View.INVISIBLE
            fh_doubleIv.visibility = View.INVISIBLE
            fh_oretationIv.visibility = View.INVISIBLE
        }
        else{
            fh_textIv.visibility = View.VISIBLE
            fh_modelIv.visibility = View.VISIBLE
            fh_zoomIv.visibility = View.VISIBLE
            fh_doubleIv.visibility = View.VISIBLE
            fh_oretationIv.visibility = View.VISIBLE
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

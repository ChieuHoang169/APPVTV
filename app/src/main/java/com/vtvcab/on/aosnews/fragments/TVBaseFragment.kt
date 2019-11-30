package com.vtvcab.on.aosnews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vtvcab.on.aosnews.MainActivity

abstract class TVBaseFragment : Fragment() {
    val baseActivity: MainActivity
        get() = activity as MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(setLayoutId(),container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData(arguments)
        initListener()
        bindData()
    }

    override fun onResume() {
        super.onResume()
        resumeFragment()
    }

    override fun onPause() {
        super.onPause()
        pauseFragment()
    }

    /*  fun enableFullScreen(){
          baseActivity.toolbar.visibility = View.GONE
          baseActivity.navigation.visibility = View.GONE
      }

      fun disableFullScreen(){
          baseActivity.toolbar.visibility = View.VISIBLE
          baseActivity.navigation.visibility = View.VISIBLE
      }*/

    fun pushFragment(containerId: Int, fragment: Fragment, data: Bundle? = null, addToBackStack : Boolean = true) {
        if (data != null){
            fragment.arguments = data
        }
        val transaction = baseActivity.supportFragmentManager.beginTransaction().replace(containerId,fragment)

        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    fun pushFragmentFromFragment(containerId: Int, fragment: Fragment, data: Bundle? = null, addToBackStack : Boolean = true){
        if (data != null){
            fragment.arguments = data
        }
        val transaction = childFragmentManager.beginTransaction().replace(containerId,fragment)

        transaction.commitAllowingStateLoss()
    }

    fun popFragment(){
        baseActivity.supportFragmentManager.popBackStack()
    }

    fun popFragmentActivity(){
        baseActivity.onBackPressed()
    }

    abstract fun setLayoutId(): Int

    abstract fun initView()

    abstract fun initData(bundle: Bundle?)

    abstract fun initListener()

    abstract fun bindData()

    abstract fun pauseFragment()

    abstract fun resumeFragment()
}
package com.vtvcab.on.aosnews.fragments

import android.os.Bundle
import com.vtvcab.on.aosnews.R
import kotlinx.android.synthetic.main.fragment_none.*

class NoneFragment : BaseFragment() {

    override fun setLayoutId(): Int = R.layout.fragment_none

    override fun initView() {

    }

    override fun initData(bundle: Bundle?) {
        llNoneContainer.setBackgroundColor(bundle!!.getInt("color"))
    }

    override fun initListener() {

    }

    override fun bindData() {

    }
}
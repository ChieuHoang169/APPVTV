package com.vtvcab.on.aosnews.fragments.tv

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.vtvcab.on.aosnews.R
import com.vtvcab.on.aosnews.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_tv2.*

class TVFragment2 : BaseFragment() {


    internal lateinit var pageAdapter: PageAdapter



    override fun setLayoutId(): Int = R.layout.fragment_tv2
    override fun initView() {


        pageAdapter = PageAdapter(childFragmentManager, tablayout.tabCount)
        viewPager.adapter = pageAdapter
        viewPager.offscreenPageLimit = 0

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))

    }

    override fun initData(bundle: Bundle?) {
        return
    }

    override fun initListener() {
        return
    }

    override fun bindData() {
        return
    }
}
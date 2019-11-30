package com.vtvcab.on.aosnews.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TVPagerAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val TAB_TITLES = arrayOf("Tất cả", "VTV", "VTVCab", "Nước ngoài", "Đặc sắc")

    private val fragments : ArrayList<Fragment> by lazy {
        ArrayList<Fragment>()
    }

    fun addFragment(fragment: Fragment, data: Bundle? = null){
        fragments.add(fragment)
        fragment.arguments = data
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TAB_TITLES[position]
    }
}
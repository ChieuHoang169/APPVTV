package com.vtvcab.on.aosnews.fragments

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.vtvcab.on.aosnews.adapter.TVPagerAdapter
import com.vtvcab.on.aosnews.util.RecyclerTabLayout
import kotlinx.android.synthetic.main.fragment_tv.*

class TVFragment : BaseFragment() {

    private val pagerAdapter: TVPagerAdapter by lazy {
        TVPagerAdapter(childFragmentManager)
    }

    private lateinit var recyclerTabLayout: RecyclerTabLayout

    private lateinit var ChildTVFragmentAll: ChildTVFragment
    private lateinit var ChildTVFragmentVTV: ChildTVFragment
    private lateinit var ChildTVFragmentVTVCab: ChildTVFragment
    private lateinit var ChildTVFragmentForeign: ChildTVFragment
    private lateinit var ChildTVFragmentFeatured: ChildTVFragment

    override fun setLayoutId(): Int = com.vtvcab.on.aosnews.R.layout.fragment_tv

    override fun initView() {
        val bundle = Bundle()
        ChildTVFragmentAll = ChildTVFragment()
        bundle.putString("mTVFragment", "all")

        val bundle1 = Bundle()
        ChildTVFragmentVTV = ChildTVFragment()
        bundle1.putString("mTVFragment", "vtv")

        val bundle2 = Bundle()
        ChildTVFragmentVTVCab = ChildTVFragment()
        bundle2.putString("mTVFragment", "vtvcab")

        val bundle3 = Bundle()
        ChildTVFragmentForeign = ChildTVFragment()
        bundle3.putString("mTVFragment", "foreign")

        val bundle4 = Bundle()
        ChildTVFragmentFeatured = ChildTVFragment()
        bundle4.putString("mTVFragment", "featured")


        pagerAdapter.addFragment(ChildTVFragmentAll,bundle)
        pagerAdapter.addFragment(ChildTVFragmentVTV,bundle1)
        pagerAdapter.addFragment(ChildTVFragmentVTVCab,bundle2)
        pagerAdapter.addFragment(ChildTVFragmentForeign,bundle3)
        pagerAdapter.addFragment(ChildTVFragmentFeatured,bundle4)

        pagerAdapter.notifyDataSetChanged()
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(mPagerChange)
        recyclerTabLayout = baseActivity.findViewById(com.vtvcab.on.aosnews.R.id.recyclerTabLayout)
        recyclerTabLayout.setBackgroundColor(resources.getColor(com.vtvcab.on.aosnews.R.color.colorBackButton))
        recyclerTabLayout.setUpWithViewPager(viewPager)
    }

    private val mPagerChange = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

            childFragmentManager.popBackStack()
        }
    }

    private lateinit var mUrl: String
    override fun initData(bundle: Bundle?) {
        mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
    }

    override fun initListener() {
        return
    }

    override fun bindData() {
        return
    }
}
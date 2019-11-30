package com.vtvcab.on.aosnews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.exoplayer2.util.TrustCertificate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vtvcab.news.fragments.CategoryFragment
import com.vtvcab.on.aosnews.adapter.MainPagerAdapter
import com.vtvcab.on.aosnews.fragments.DiscoverFragment2
import com.vtvcab.on.aosnews.fragments.PersonalFragment
import com.vtvcab.on.aosnews.fragments.WatchFragment2
import com.vtvcab.on.aosnews.fragments.tv.TVFragment2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_child_tv.*
import kotlinx.android.synthetic.main.fragment_watch.*
import kotlinx.android.synthetic.main.layout_media_controller.*
import kotlinx.android.synthetic.main.layout_media_controller_watch.*

class MainActivity : AppCompatActivity() {
    private val mPagerAdapter: MainPagerAdapter by lazy {
        MainPagerAdapter(supportFragmentManager)
    }

    var navigationBarHeight = 0
    private lateinit var categoryFragment: CategoryFragment
    private lateinit var tvFragment: TVFragment2
    private lateinit var watchFragment: WatchFragment2
    private lateinit var discoverFragment: DiscoverFragment2
    private lateinit var personalFragment: PersonalFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        TrustCertificate.excute()
        if(savedInstanceState == null){
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            categoryFragment = CategoryFragment()
            tvFragment = TVFragment2()
            watchFragment = WatchFragment2()
            discoverFragment = DiscoverFragment2()
            personalFragment = PersonalFragment()
            mPagerAdapter.addFragment(categoryFragment)
            mPagerAdapter.addFragment(tvFragment)
            mPagerAdapter.addFragment(watchFragment)
            mPagerAdapter.addFragment(discoverFragment)
            mPagerAdapter.addFragment(personalFragment)

            mPagerAdapter.notifyDataSetChanged()
            vpMain.setSwipePagingEnabled(false)
            vpMain.adapter = mPagerAdapter
            vpMain.addOnPageChangeListener(mPagerChange)
            vpMain.offscreenPageLimit = 0

        }
    }

    private fun checkPlayVideoDX(){
        if (videoPlayerDX?.player?.playWhenReady == true){
            videoPlayerDX?.player?.playWhenReady = false
            imgPlayDX.setImageResource(R.drawable.ic_play)
        }
    }

    private fun checkPlayVideoTH(){
        if (videoPlayerTH?.player?.playWhenReady == true){
            imgPlay.setImageResource(R.drawable.ic_play)
            videoPlayerTH?.player?.playWhenReady = false
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_kho_video -> {
                vpMain.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_truyen_hinh -> {
                vpMain.currentItem = 1

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_don_xem -> {
                vpMain.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_kham_pha -> {
                vpMain.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ca_nhan -> {
                vpMain.currentItem = 4
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val mPagerChange = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            navigation.selectedItemId = navigation.menu.getItem(position).itemId

            supportFragmentManager.popBackStack()
            checkPlayVideoDX()
            checkPlayVideoTH()
        }
    }

    override fun onBackPressed() {
        if (vpMain.currentItem == 0) {
            if (supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        } else {
            vpMain.currentItem = 0
        }
    }
}

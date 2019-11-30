package com.vtvcab.on.aosnews.fragments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.vtvcab.on.aosnews.R
import com.vtvcab.on.aosnews.adapter.VideoPlayerRecyclerAdapter
import com.vtvcab.on.aosnews.adapter.VideoPlayerRecyclerView
import com.vtvcab.on.aosnews.model.MediaObject
import com.vtvcab.on.aosnews.util.VerticalSpacingItemDecorator

class WatchFragment2 : BaseFragment(){

    private val nicCageMovies = listOf(
        MediaObject("The Flash",
            "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8",
            "http://103.233.48.47:6003/img/discovery/flash.png",
            "The Flash là một series phim truyền hình Mỹ, được phát triển bởi nhà văn/nhà sản xuất Greg Berlanti, Andrew Kreisberg và Geoff Johns, được phát sóng trên The CW.","Sắp chiếu ngày 29 tháng 2"),
        MediaObject("Small Ville",
            "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8",
            "http://103.233.48.47:6003/img/discovery/smallville.png",
            "Năm 1989, sau một trận mưa băng rơi xuống thị trấn Smallville giết chết rất nhiều người, gia đình ông bà Jonathan và Martha Ken đã tìm thấy một chiếc phi thuyền rơi xuống cánh đồng, bên trong đó có một cậu bé xinh xắn. Họ đem đứa bé về nuôi và đặt tên là Clark Kent.","Sắp chiếu ngày 29 tháng 2"),
        MediaObject("The Flash",
            "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8",
            "http://103.233.48.47:6003/img/discovery/flash.png",
            "The Flash là một series phim truyền hình Mỹ, được phát triển bởi nhà văn/nhà sản xuất Greg Berlanti, Andrew Kreisberg và Geoff Johns, được phát sóng trên The CW.","Sắp chiếu ngày 29 tháng 2"),
        MediaObject("Small Ville",
            "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8",
            "http://103.233.48.47:6003/img/discovery/smallville.png",
            "Năm 1989, sau một trận mưa băng rơi xuống thị trấn Smallville giết chết rất nhiều người, gia đình ông bà Jonathan và Martha Ken đã tìm thấy một chiếc phi thuyền rơi xuống cánh đồng, bên trong đó có một cậu bé xinh xắn. Họ đem đứa bé về nuôi và đặt tên là Clark Kent.","Sắp chiếu ngày 29 tháng 2"),
        MediaObject("Small Ville",
            "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8",
            "http://103.233.48.47:6003/img/discovery/smallville.png",
            "Năm 1989, sau một trận mưa băng rơi xuống thị trấn Smallville giết chết rất nhiều người, gia đình ông bà Jonathan và Martha Ken đã tìm thấy một chiếc phi thuyền rơi xuống cánh đồng, bên trong đó có một cậu bé xinh xắn. Họ đem đứa bé về nuôi và đặt tên là Clark Kent.","Sắp chiếu ngày 29 tháng 2")
    )

    private var mRecyclerView: VideoPlayerRecyclerView? = null
    private var mAdapterWatch : VideoPlayerRecyclerAdapter? = null

    override fun setLayoutId(): Int = R.layout.fragment_watch2

    override fun initView() {
        mRecyclerView = baseActivity.findViewById(R.id.recycler_view)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this.baseActivity)
        mRecyclerView?.setLayoutManager(layoutManager)
        val itemDecorator = VerticalSpacingItemDecorator(10)
        mRecyclerView?.addItemDecoration(itemDecorator)

        mRecyclerView?.setMediaObjects(nicCageMovies)
        mAdapterWatch = VideoPlayerRecyclerAdapter(nicCageMovies, initGlide())
        mAdapterWatch!!.notifyDataSetChanged()
        mRecyclerView?.setAdapter(mAdapterWatch)
    }

    private fun initGlide(): RequestManager {
        val options = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)

        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser){
//            mAdapterWatch!!.notifyDataSetChanged()
            mRecyclerView?.onRestartPlayer()
        } else {
            mRecyclerView?.onPausePlayer()
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    private lateinit var mUrl: String
    override fun initData(bundle: Bundle?) {
//        mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
        return
    }

    override fun initListener() {
        return
    }

    override fun bindData() {
        return
    }
}
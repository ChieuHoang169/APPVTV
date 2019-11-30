package com.vtvcab.on.aosnews.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vtvcab.on.aosnews.R
import com.vtvcab.on.aosnews.adapter.DiscoverAdapter
import com.vtvcab.on.aosnews.model.User
import kotlinx.android.synthetic.main.fragment_child_tv.*


class DiscoverFragment2: BaseFragment() {

    override fun setLayoutId(): Int = R.layout.fragment_discover2

    @SuppressLint("WrongConstant")
    override fun initView() {
        videoPlayerTH?.visibility = View.GONE
        val recyclerView = baseActivity.findViewById(R.id.list_recycler_view) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        val users = ArrayList<User>()

        //adding some dummy data to the list
        users.add(User("0","http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8","Sắp chiếu ngày 29 tháng 2", "The Flash", "The Flash là một series phim truyền hình Mỹ, được phát triển bởi nhà văn/nhà sản xuất Greg Berlanti, Andrew Kreisberg và Geoff Johns, được phát sóng trên The CW.", "Hành động", "Phiêu lưu", "Chính kịch"))
        users.add(User("1","http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8","Sắp chiếu ngày 29 tháng 2", "Small Ville", "Năm 1989, sau một trận mưa băng rơi xuống thị trấn Smallville giết chết rất nhiều người, gia đình ông bà Jonathan và Martha Ken đã tìm thấy một chiếc phi thuyền rơi xuống cánh đồng, bên trong đó có một cậu bé xinh xắn. Họ đem đứa bé về nuôi và đặt tên là Clark Kent.", "Hành động", "Phiêu lưu", "Chính kịch"))
        users.add(User("2","http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8","Sắp chiếu ngày 29 tháng 2", "The Flash", "The Flash là một series phim truyền hình Mỹ, được phát triển bởi nhà văn/nhà sản xuất Greg Berlanti, Andrew Kreisberg và Geoff Johns, được phát sóng trên The CW.", "Hành động", "Phiêu lưu", "Chính kịch"))
        users.add(User("3","http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8","Sắp chiếu ngày 29 tháng 2", "Small Ville", "Năm 1989, sau một trận mưa băng rơi xuống thị trấn Smallville giết chết rất nhiều người, gia đình ông bà Jonathan và Martha Ken đã tìm thấy một chiếc phi thuyền rơi xuống cánh đồng, bên trong đó có một cậu bé xinh xắn. Họ đem đứa bé về nuôi và đặt tên là Clark Kent.", "Hành động", "Phiêu lưu", "Chính kịch"))

        //creating our adapter
        val adapter = DiscoverAdapter(users)

        adapter.notifyDataSetChanged()
        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
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
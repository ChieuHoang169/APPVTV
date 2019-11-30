package com.vtvcab.on.aosnews.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.VideoPlayerView
import com.vtvcab.on.aosnews.R
import com.vtvcab.on.aosnews.model.User
import java.util.*



class DiscoverAdapter(val userList: ArrayList<User>) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    private var arrPosSelected: BooleanArray? = null


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.ViewHolder {
        arrPosSelected = BooleanArray(userList.size)
        val v = LayoutInflater.from(parent.context).inflate(com.vtvcab.on.aosnews.R.layout.item_list_discover, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: DiscoverAdapter.ViewHolder, position: Int) {
        var mUrl: String = ""
        val user = userList.get(position)
        holder.bindItems(userList[position])
        mUrl = user.mUrl
        holder.videoPlayerItemWatch?.player?.playWhenReady = false
        holder.videoPlayerItemWatch?.setVideoUri(mUrl, "")
        //            videoPlayerItemWatch.player.addListener(playerListener)
        holder.videoPlayerItemWatch?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if (holder.includeItemWatch?.visibility == View.INVISIBLE) {
                    holder.includeItemWatch?.visibility = View.VISIBLE

                    val handler = Handler()
                    handler.postDelayed({
                        holder.includeItemWatch?.visibility = View.INVISIBLE
                    }, 2000)
//                    notifyDataSetChanged()
                } else {
                    holder.includeItemWatch?.visibility = View.INVISIBLE
                }
            }
        })

        holder.imgPlayItemDiscover?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if (holder.videoPlayerItemWatch != null) {
                    for (i in 0 ..arrPosSelected!!.size - 1 ) {
                        arrPosSelected!![i] = i == position && !arrPosSelected!![i];
                    }
                 /*   val player = holder.videoPlayerItemWatch?.player
                    if (player != null
                                && player.playbackState != Player.STATE_ENDED
                                && player.playbackState != Player.STATE_IDLE
                                && player.playWhenReady) {
                        holder.imgPlayItemDiscover?.setImageResource(R.drawable.ic_play)
                        holder.videoPlayerItemWatch?.player?.playWhenReady = false
                    } else {
                        holder.imgPlayItemDiscover?.setImageResource(R.drawable.ic_pause)
                        holder.videoPlayerItemWatch?.player?.playWhenReady = true
                    }*/
                    notifyDataSetChanged()

                    val handler = Handler()
                    handler.removeCallbacks {
                        holder.includeItemWatch?.visibility = View.INVISIBLE
                    }
                    handler.postDelayed({
                        holder.includeItemWatch?.visibility = View.INVISIBLE
                    }, 2000)
                }
            }

        })

        if (arrPosSelected!![position]){
            holder.imgPlayItemDiscover?.setImageResource(R.drawable.ic_pause)
            holder.videoPlayerItemWatch?.player?.playWhenReady = true
        } else{
            holder.imgPlayItemDiscover?.setImageResource(R.drawable.ic_play)
            holder.videoPlayerItemWatch?.player?.playWhenReady = false
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var videoPlayerItemWatch: VideoPlayerView? = null
        private var tvTimeDate: TextView? = null
        private var tvTitle: TextView? = null
        private var tvContent: TextView? = null
        private var tvAction: TextView? = null
        private var tvAdventure: TextView? = null
        private var tvDrama: TextView? = null
        var includeItemWatch: RelativeLayout? = null
        var imgPlayItemDiscover: ImageView? = null


        init {
            videoPlayerItemWatch = itemView.findViewById(com.vtvcab.on.aosnews.R.id.videoPlayerItemWatch) as VideoPlayerView
            tvTimeDate = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvTimeDate) as TextView
            tvTitle  = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvTitle) as TextView
            tvContent = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvContent) as TextView
            tvAction  = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvAction) as TextView
            tvAdventure = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvAdventure) as TextView
            tvDrama  = itemView.findViewById(com.vtvcab.on.aosnews.R.id.tvDrama) as TextView
            includeItemWatch = itemView.findViewById(com.vtvcab.on.aosnews.R.id.includeItemWatch) as RelativeLayout
            imgPlayItemDiscover = itemView.findViewById<ImageView>(com.vtvcab.on.aosnews.R.id.imgPlayItemDiscover)
        }

        fun bindItems(user: User) {

            tvTimeDate?.text = user.mDateTime
            tvTitle?.text = user.mTitle
            tvContent?.text = user.mContent
            tvAction?.text = user.mAction
            tvAdventure?.text = user.mAdventure
            tvDrama?.text = user.mDrama

        }
    }
}
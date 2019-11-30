@file:Suppress("DEPRECATION")

package com.vtvcab.on.aosnews.fragments

import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.exoplayer2.Player
import com.vtvcab.on.aosnews.R
import com.vtvcab.on.aosnews.activity.FullScreenActivity
import com.vtvcab.on.aosnews.util.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_media_controller.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class DetailFragment : BaseFragment(), View.OnClickListener, View.OnTouchListener {
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var moveX: Float = 0F
    private var moveY: Float = 0F
    private var minDistance = 10f
    internal var max = 500f
    private var oldDeltaY = -1f

    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private var viewScaleHeight = 0
    private var viewScaleWidth = 0
    private var viewScaleX = 0F
    private var isMinimize = false

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view.id == R.id.rlVideoPlayer || view.id == R.id.llTitle) {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("ACTION: ", "DOWNNNNNNNN")
                    oldDeltaY = -1f
                    viewScaleHeight = viewScaleTop.height
                    viewScaleWidth = viewScaleTop.width
                    viewScaleX = viewScaleTop.x
                    downX = motionEvent.rawX
                    downY = motionEvent.rawY
                    return true
                }

                MotionEvent.ACTION_UP -> {
                    Log.e("ACTION: ", "UPPPPPPPPPPPPPP")
                    val upX = motionEvent.rawX
                    val upY = motionEvent.rawY
                    val deltaUpX = downX - upX
                    val deltaUpY = downY - upY

                 /*   if (isMinimize && Math.abs(deltaUpX) > Math.abs(deltaUpY)) {
                        if (deltaUpX > 100) {
                            swipeToClose()
                            return false
                        }
                        return true
                    }*/

                    if (Math.abs(deltaUpX) < minDistance && Math.abs(deltaUpY) < minDistance) {
                        if (isMinimize) {
                            scaleAnimator(viewScaleTop.layoutParams.height, 1, viewScaleTop.layoutParams.width, 1)
                        } else {
                            if (rlMediaController.visibility == View.VISIBLE) {
                                hideController()
                            } else {
                                showController()
                                handler.postDelayed(hideControllerRunnable,2000)
                            }
                        }
                        isMinimize = false
                    } else {
                        if (viewScaleTop.layoutParams.height < screenHeight / 3f) {
                            if (viewScaleTop.layoutParams.height < 0) {
                                viewScaleTop.layoutParams.height = 1
                                viewScaleTop.layoutParams.width = 1
                            } else {
                                scaleAnimator(viewScaleTop.layoutParams.height, 1, viewScaleTop.layoutParams.width, 1)
                            }
                            isMinimize = false
                            return false
                        } else {
                            scaleAnimator(
                                viewScaleTop.layoutParams.height,
                                (screenHeight - screenHeight / 5f - bottomBarHeight - baseActivity.navigationBarHeight + 40).toInt(),
                                viewScaleTop.layoutParams.width,
                                (screenWidth - screenWidth / 2f).toInt()
                            )
                            isMinimize = true
                            return false
                        }
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    moveX = motionEvent.rawX
                    moveY = motionEvent.rawY

                    val deltaX = downX - moveX
                    val deltaY = downY - moveY

                    //VERTICAL SCROLL
                    if (Math.abs(deltaX) <= Math.abs(deltaY)) {
                        if (deltaY > 0 && deltaY > minDistance) {
                            hideController()
                            Log.e("DELTA Y > 0 : ", deltaY.toString())
                            Log.e("HEIGHT : ", viewScaleTop.layoutParams.height.toString())
                            val newHeight = (viewScaleHeight - deltaY).toInt()
                            Log.e("NEW HEIGHT : ", newHeight.toString())
                            viewScaleTop.layoutParams.height = newHeight
                            viewScaleTop.layoutParams.width =
                                (java.lang.Float.valueOf(newHeight.toFloat()) * (9f / 24f)).toInt()
                            viewScaleTop.requestLayout()
                            rlVideoPlayer.layoutParams.height = 0
                            rlVideoPlayer.layoutParams.width = 0
                            rlVideoPlayer.requestLayout()
                            llTitle.visibility = View.GONE
                            return true
                        } else if (Math.abs(deltaY) > minDistance) {
                            hideController()
                            if (!isMinimize) {
                                Log.e("DELTA Y < 0 : ", deltaY.toString())
                                val newHeight = Math.abs(deltaY).toInt()
                                viewScaleTop.layoutParams.height = newHeight
                                viewScaleTop.layoutParams.width =
                                    (java.lang.Float.valueOf(newHeight.toFloat()) * (9f / 24f)).toInt()
                                viewScaleTop.requestLayout()

                                llTitle.visibility = View.VISIBLE
                                rlVideoPlayer.layoutParams.height = 150
                                rlVideoPlayer.layoutParams.width = 300
                                rlVideoPlayer.requestLayout()

                                llTitle.layoutParams.height = 150
                                llTitle.layoutParams.width = screenWidth - 300
                                llTitle.requestLayout()


                                Log.e("HeightviewScaleTop : ", newHeight.toString())
                                Log.e("WidthviewScaleTop : ", (java.lang.Float.valueOf(newHeight.toFloat()) * (9f / 24f)).toInt().toString())
                                Log.e("HeightrlVideoPlayer : ", (screenHeight - newHeight).toString())
                                Log.e("WidthrlVideoPlayer : ", (screenWidth - (java.lang.Float.valueOf(newHeight.toFloat()) * (9f / 24f)).toInt()).toString())
                                return true
                            }
                            return true
                        }
                    }
                }
            }
            return false
        }
        return false
    }

    private fun swipeToClose() {
        val anim = ValueAnimator.ofInt(viewScaleTop.width, 1)
        anim.addUpdateListener { valueAnimator ->
            viewScaleTop.layoutParams.width = valueAnimator.animatedValue as Int
            viewScaleTop.requestLayout()
        }
        anim.duration = 200
        anim.start()
        Handler().postDelayed({ popFragment() }, 200)
        isMinimize = false
    }

    private fun scaleAnimator(fromH: Int, toH: Int, fromW: Int, toW: Int) {
        val anim = ValueAnimator.ofInt(fromH, toH)
        anim.addUpdateListener { valueAnimator ->
            viewScaleTop.layoutParams.height = valueAnimator.animatedValue as Int
            viewScaleTop.requestLayout()
        }
        anim.duration = 500
        val animW = ValueAnimator.ofInt(fromW, toW)
        animW.addUpdateListener { valueAnimator ->
            viewScaleTop.layoutParams.width = valueAnimator.animatedValue as Int
            viewScaleTop.requestLayout()
        }
        animW.duration = 500

        anim.start()
        animW.start()
    }

    override fun setLayoutId(): Int = R.layout.fragment_detail

    private var bottomBarHeight = 0
    override fun initView() {
        screenHeight = Utils.getScreenHeight(context)
        screenWidth = Utils.getScreenWidth(context)
        llTitle.visibility = View.GONE
//        bottomBarHeight = baseActivity.navigation.height
        setupWebview()
    }

    private lateinit var mUrl: String
    override fun initData(bundle: Bundle?) {
        mUrl = bundle?.getString("detailUrl")!!
    }

    override fun initListener() {
        imgPlay.setOnClickListener(this)
        imgZoomOut.setOnClickListener(this)
        failedView.setOnClickListener(this)
        img_close_detail.setOnClickListener(this)
        img_pause_detail.setOnClickListener(this)
        rlVideoPlayer.setOnTouchListener(this)
        llTitle.setOnTouchListener(this)
        webDetail.setOnTouchListener(this)
    }

    private val playbackObservable = Observable.interval(1, TimeUnit.SECONDS)
        .map { videoPlayer?.player?.currentPosition }
        .observeOn(AndroidSchedulers.mainThread())

    override fun bindData() {
        failedView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        shimmer_view_container.visibility = View.VISIBLE
        flShimmer.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()

//        mUrl = "http://103.233.48.44:8080/live/vtv1_enc/index.m3u8"
        videoPlayer.setVideoUri(mUrl, "")

        videoPlayer.player.addListener(playerListener)

/*        NewsApplication.getApiService().getUrl("detailvideo.html").enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                failedView.visibility = View.VISIBLE
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        webDetail?.loadDataWithBaseURL(null, response.body(), "text/html", "UTF-8", null)
                    } else {
                        failedView.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                    }
                } else {
                    failedView.visibility = View.VISIBLE
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                }
            }
        })*/
        webDetail!!.loadUrl("https://dothocam.000webhostapp.com/detailVideo/detailvideo.html")
    }

    private fun setupWebview() {
        val webSetting = webDetail.settings
        webSetting.javaScriptEnabled = true
        webDetail.webChromeClient = detailChromeClient
        webDetail.webViewClient = webViewClient
    }

    private fun goToDetail(url: String) {
        val bundle = Bundle()
        bundle.putString("detailUrl", url)

        baseActivity.onBackPressed()
        pushFragment(R.id.homeContainer, DetailFragment(), bundle, true)
    }

    private fun goToFullScreen(url: String) {
        val intent = Intent(baseActivity, FullScreenActivity::class.java)
        intent.putExtra("detailUrl", url)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            videoPlayer?.player?.seekTo(data.getLongExtra("currentPosition", videoPlayer.player.duration))
        }
    }

    private val detailChromeClient = object : WebChromeClient() {
        override fun onJsAlert(view: WebView?, url: String?, message: String, result: JsResult): Boolean {
            result.confirm()
            goToDetail(message)
            return true
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            Handler().postDelayed({
                if (shimmer_view_container != null) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    flShimmer.visibility = View.GONE
                }
                if (webDetail != null) {
                    webDetail.visibility = View.VISIBLE
                }
                super.onPageFinished(view, url)
            }, 500)
        }
    }

    private lateinit var playbackDisposable: Disposable
    private val playerListener = object : Player.DefaultEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                loadingView?.visibility = View.GONE
                videoPlayer?.visibility = View.VISIBLE
            } else if (playbackState == Player.STATE_ENDED) {

            }
        }
    }

    override fun onStop() {
        super.onStop()
        videoPlayer?.player?.playWhenReady = false
        if (!playbackDisposable?.isDisposed) {
            playbackDisposable?.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer?.releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer?.player?.playWhenReady = true
        imgPlay.setImageResource(R.drawable.ic_pause)
        playbackDisposable = playbackObservable.subscribe { progress ->
            if (progress != null) {
                progress_bar_video.progress =
                    ((progress.toFloat() / videoPlayer.player.duration.toFloat()) * 100).toInt()

                tvCurrentTime.text = convertTimeFormat(progress)
                tvDuration.text = convertTimeFormat(videoPlayer.player.duration)
            }
        }
    }

    private fun convertTimeFormat(millisecond: Long): String {
        val format = SimpleDateFormat("mm:ss", Locale.US)
        return format.format(Date(millisecond))
    }

    override fun onClick(view: View) {
        when (view) {
            imgPlay -> {
                if (videoPlayer != null) {
                    isPausePlayVideo()
                    handler.postDelayed(hideControllerRunnable,2000)
                }
            }

            imgZoomOut -> {
                goToFullScreen(mUrl)
            }

            failedView -> {
                bindData()
            }

            img_close_detail -> {
                Handler().postDelayed({ popFragment() }, 200)
                isMinimize = false
            }

            img_pause_detail -> {
                if (videoPlayer != null) {
                    isPausePlayVideo()
                }
            }
        }
    }

    private fun isPausePlayVideo(){
        if (isPlaying()) {
            imgPlay.setImageResource(R.drawable.ic_play)
            img_pause_detail.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            videoPlayer.player.playWhenReady = false
        } else {
            imgPlay.setImageResource(R.drawable.ic_pause)
            img_pause_detail.setImageResource(R.drawable.ic_pause_white_24dp)
            videoPlayer.player.playWhenReady = true
        }
        handler.removeCallbacks(hideControllerRunnable)
    }

    private fun hideController(){
        rlMediaController?.visibility = View.GONE
    }

    private fun showController(){
        rlMediaController?.visibility = View.VISIBLE
    }

    private val hideControllerRunnable = Runnable {
        rlMediaController?.visibility = View.GONE
    }

    private val handler by lazy {
        Handler()
    }

    private fun isPlaying(): Boolean {
        return if (videoPlayer != null) {
            val player = videoPlayer.player
            (player != null
                    && player.playbackState != Player.STATE_ENDED
                    && player.playbackState != Player.STATE_IDLE
                    && player.playWhenReady)
        } else {
            false
        }
    }
}
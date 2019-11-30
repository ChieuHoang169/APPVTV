package com.vtvcab.on.aosnews.fragments.tv

import android.annotation.SuppressLint
import android.app.Activity
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
import com.vtvcab.on.aosnews.fragments.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_featured.*
import kotlinx.android.synthetic.main.layout_media_controller_watch.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FeaturedFragment : BaseFragment(), View.OnClickListener, View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view.id == R.id.videoPlayerFeatured) {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("ACTIONPhong: ", "UPPPPPPPPPPPPPP")
                    if (includeFeatured.visibility == View.INVISIBLE) {
                        showController()
                        handler.postDelayed(hideControllerRunnable, 2000)
                    } else {
                        hideController()
                    }
                }
            }
            return false
        }
        return false
    }

    override fun setLayoutId(): Int = R.layout.fragment_featured

    private var bottomBarHeight = 0
    override fun initView() {
        bottomBarHeight = baseActivity.navigation.height
        setupWebview()
    }

    private lateinit var mUrl: String
    override fun initData(bundle: Bundle?) {
        mUrl = "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8"
    }

    override fun initListener() {
        imgPlayDX.setOnClickListener(this)
        imgZoomOutDX.setOnClickListener(this)
        videoPlayerFeatured.setOnTouchListener(this)
        failedViewFeatured.setOnClickListener(this)
    }

    private val playbackObservable = Observable.interval(1, TimeUnit.SECONDS)
        .map { videoPlayerFeatured?.player?.currentPosition }
        .observeOn(AndroidSchedulers.mainThread())

    override fun bindData() {
        return
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser){
            videoPlayerFeatured?.visibility = View.VISIBLE
            failedViewFeatured?.visibility = View.GONE
            loadingViewFeatured?.visibility = View.VISIBLE
            shimmer_view_containerFeatured?.visibility = View.VISIBLE
            flShimmerFeatured?.visibility = View.VISIBLE
            shimmer_view_containerFeatured?.startShimmer()

            setPlayVideoDX()
            callAPIWatch("lineTV.html")
        } else{
            videoPlayerFeatured?.visibility = View.GONE
            videoPlayerFeatured?.player?.playWhenReady = false
            Log.i("VTVFragmen", "onPauseFragment()");
//            Toast.makeText(baseActivity, "onPauseFragment():" + "VTVFragmen", Toast.LENGTH_SHORT).show();
        }

        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun setPlayVideoDX() {
        // mUrl = "http://103.233.48.44:8080/live/vtv1_enc/index.m3u8"
        videoPlayerFeatured?.setVideoUri(mUrl, "")

        videoPlayerFeatured?.player?.addListener(playerListener)
    }

    private fun callAPIWatch(url: String) {
      /*  NewsApplication.getApiService().getUrl(url).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                failedViewFeatured.visibility = View.VISIBLE
                shimmer_view_containerFeatured.stopShimmer()
                shimmer_view_containerFeatured.visibility = View.GONE
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        webDetailFeatured?.loadDataWithBaseURL(null, response.body(), "text/html", "UTF-8", null)
                    } else {
                        failedViewFeatured.visibility = View.VISIBLE
                        shimmer_view_containerFeatured.stopShimmer()
                        shimmer_view_containerFeatured.visibility = View.GONE
                    }
                } else {
                    failedViewFeatured.visibility = View.VISIBLE
                    shimmer_view_containerFeatured.stopShimmer()
                    shimmer_view_containerFeatured.visibility = View.GONE
                }
            }
        })*/
        webDetailFeatured!!.loadUrl("https://dothocam.000webhostapp.com/category/lineTV.html")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebview() {
        val webSetting = webDetailFeatured.settings
        webSetting.javaScriptEnabled = true
        webDetailFeatured.webChromeClient = detailChromeClient
        webDetailFeatured.webViewClient = webViewClient
    }

    private fun goToDetail(url: String) {
        val bundle = Bundle()
        bundle.putString("detailUrl", url)

        mUrl = url
        loadingViewFeatured.visibility = View.VISIBLE
        setPlayVideoDX()

//        baseActivity.onBackPressed()
//        pushFragment(R.id.homeContainerTH, DetailFragment(), bundle, true)
    }

    private fun goToFullScreen(url: String) {
        val intent = Intent(baseActivity, FullScreenActivity::class.java)
        intent.putExtra("detailUrl", url)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            videoPlayerFeatured?.player?.seekTo(data.getLongExtra("currentPosition", videoPlayerFeatured.player.duration))
        }
    }

    private val detailChromeClient = object : WebChromeClient() {
        override fun onJsAlert(view: WebView?, url: String?, message: String, result: JsResult): Boolean {
            result.confirm()
            Log.d("TAG", "message" + message)
            goToDetail(message)
            return true
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            Handler().postDelayed({
                if (shimmer_view_containerFeatured != null) {
                    shimmer_view_containerFeatured.stopShimmer()
                    shimmer_view_containerFeatured.visibility = View.GONE
                    flShimmerFeatured.visibility = View.GONE
                }
                if (webDetailFeatured != null) {
                    webDetailFeatured.visibility = View.VISIBLE
                }
                super.onPageFinished(view, url)
            }, 500)
        }
    }

    private lateinit var playbackDisposable: Disposable
    private val playerListener = object : Player.DefaultEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                loadingViewFeatured?.visibility = View.GONE
                videoPlayerFeatured?.visibility = View.VISIBLE
            } else if (playbackState == Player.STATE_ENDED) {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("PAUSE: ", "onPauseeeeeeeeee")
        videoPlayerFeatured?.player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        videoPlayerFeatured?.player?.playWhenReady = false
        if (!playbackDisposable?.isDisposed) {
            playbackDisposable?.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerFeatured?.releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        videoPlayerFeatured?.player?.playWhenReady = false
        imgPlayDX.setImageResource(R.drawable.ic_play)
        playbackDisposable = playbackObservable.subscribe { progress ->
            if (progress != null) {
                progress_bar_videoDX.progress =
                    ((progress.toFloat() / videoPlayerFeatured.player.duration.toFloat()) * 100).toInt()

                tvCurrentTimeDX.text = convertTimeFormat(progress)
                tvDurationDX.text = convertTimeFormat(videoPlayerFeatured.player.duration)
            }
        }
    }

    private fun convertTimeFormat(millisecond: Long): String {
        val format = SimpleDateFormat("mm:ss", Locale.US)
        return format.format(Date(millisecond))
    }

    override fun onClick(view: View) {
        when (view) {
            imgPlayDX -> {
                if (videoPlayerFeatured != null) {
                    if (isPlaying()) {
                        imgPlayDX.setImageResource(R.drawable.ic_play)
                        videoPlayerFeatured.player.playWhenReady = false
                    } else {
                        imgPlayDX.setImageResource(R.drawable.ic_pause)
                        videoPlayerFeatured.player.playWhenReady = true
                    }
                    handler.removeCallbacks(hideControllerRunnable)
                    handler.postDelayed(hideControllerRunnable, 2000)
                }
            }

            imgZoomOutDX -> {
                goToFullScreen(mUrl)
            }

            failedViewFeatured -> {
                bindData()
            }
        }
    }

    private fun hideController() {
        includeFeatured?.visibility = View.INVISIBLE
    }

    private fun showController() {
        includeFeatured?.visibility = View.VISIBLE
    }

    private val hideControllerRunnable = Runnable {
        includeFeatured?.visibility = View.INVISIBLE
    }

    private val handler by lazy {
        Handler()
    }

    private fun isPlaying(): Boolean {
        return if (videoPlayerFeatured != null) {
            val player = videoPlayerFeatured.player
            (player != null
                    && player.playbackState != Player.STATE_ENDED
                    && player.playbackState != Player.STATE_IDLE
                    && player.playWhenReady)
        } else {
            false
        }
    }
}
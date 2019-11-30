package com.vtvcab.on.aosnews.fragments


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
import com.vtvcab.on.aosnews.application.NewsApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_child_tv.*
import kotlinx.android.synthetic.main.layout_media_controller.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChildTVFragment : BaseFragment(), View.OnClickListener, View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view.id == R.id.videoPlayerTH) {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("ACTIONPhong: ", "UPPPPPPPPPPPPPP")
                    if (include.visibility == View.INVISIBLE) {
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

    override fun setLayoutId(): Int = R.layout.fragment_child_tv

    private lateinit var mId: String
    override fun initView() {
        setupWebview()
    }

    private lateinit var mUrl: String
    override fun initData(bundle: Bundle?) {
        mId = bundle?.getString("mTVFragment")!!
        mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
    }

    override fun initListener() {
        imgPlay.setOnClickListener(this)
        imgZoomOut.setOnClickListener(this)
        videoPlayerTH.setOnTouchListener(this)
        failedView.setOnClickListener(this)
    }

    private val playbackObservable = Observable.interval(1, TimeUnit.SECONDS)
        .map { videoPlayerTH?.player?.currentPosition }
        .observeOn(AndroidSchedulers.mainThread())

    override fun bindData() {
        failedView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        shimmer_view_container.visibility = View.VISIBLE
        flShimmer.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()

        if (mId == "all") {
            mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineTV.html")
        } else if (mId == "vtv") {
            mUrl = "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineVTV.html")
        } else if (mId == "vtvcad") {
            mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineVTVCab.html")
        } else if (mId == "foreign") {
            mUrl = "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineTV.html")
        } else if (mId == "featured") {
            mUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineVTV.html")
        } else {
            mUrl = "http://liverestreamobj.5b1df984.cdnviet.com/hls/VTV1_HD_TEST/index.m3u8"
            setPlayVideoTH()
            callAPILiveTV("lineTV.html")
        }
    }

    private fun setPlayVideoTH() {
        // mUrl = "http://103.233.48.44:8080/live/vtv1_enc/index.m3u8"
        videoPlayerTH.setVideoUri(mUrl, "")

        videoPlayerTH.player.addListener(playerListener)
    }

    private fun callAPILiveTV(url: String) {
        NewsApplication.getApiService().getUrl(url).enqueue(object : Callback<String> {
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
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebview() {
        val webSetting = webDetail.settings
        webSetting.javaScriptEnabled = true
        webDetail.webChromeClient = detailChromeClient
        webDetail.webViewClient = webViewClient
    }

    private fun goToDetail(url: String) {
        val bundle = Bundle()
        bundle.putString("detailUrl", url)

        mUrl = url
        loadingView.visibility = View.VISIBLE
        setPlayVideoTH()

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
            videoPlayerTH?.player?.seekTo(data.getLongExtra("currentPosition", videoPlayerTH.player.duration))
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
                videoPlayerTH?.visibility = View.VISIBLE
            } else if (playbackState == Player.STATE_ENDED) {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("PAUSE: ", "onPauseeeeeeeeee")
        videoPlayerTH?.player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        videoPlayerTH?.player?.playWhenReady = false
        if (!playbackDisposable?.isDisposed) {
            playbackDisposable?.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerTH?.releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        videoPlayerTH?.player?.playWhenReady = false
        playbackDisposable = playbackObservable.subscribe { progress ->
            if (progress != null) {
                progress_bar_video.progress =
                    ((progress.toFloat() / videoPlayerTH.player.duration.toFloat()) * 100).toInt()

                tvCurrentTime.text = convertTimeFormat(progress)
                tvDuration.text = convertTimeFormat(videoPlayerTH.player.duration)
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
                if (videoPlayerTH != null) {
                    if (isPlaying()) {
                        imgPlay.setImageResource(R.drawable.ic_play)
                        videoPlayerTH.player.playWhenReady = false
                    } else {
                        imgPlay.setImageResource(R.drawable.ic_pause)
                        videoPlayerTH.player.playWhenReady = true
                    }
                    handler.removeCallbacks(hideControllerRunnable)
                    handler.postDelayed(hideControllerRunnable, 2000)
                }
            }

            imgZoomOut -> {
                goToFullScreen(mUrl)
            }

            failedView -> {
                bindData()
            }
        }
    }

    private fun hideController() {
        include?.visibility = View.INVISIBLE
    }

    private fun showController() {
        include?.visibility = View.VISIBLE
    }

    private val hideControllerRunnable = Runnable {
        include?.visibility = View.INVISIBLE
    }

    private val handler by lazy {
        Handler()
    }

    private fun isPlaying(): Boolean {
        return if (videoPlayerTH != null) {
            val player = videoPlayerTH.player
            (player != null
                    && player.playbackState != Player.STATE_ENDED
                    && player.playbackState != Player.STATE_IDLE
                    && player.playWhenReady)
        } else {
            false
        }
    }
}
package com.vtvcab.on.aosnews.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.vtvcab.on.aosnews.R
import kotlinx.android.synthetic.main.fragment_category.*

class PersonalFragment : BaseFragment(), ViewTreeObserver.OnScrollChangedListener {
    override fun onScrollChanged() {
        swipeRefreshContainer?.isEnabled = webHome.scrollY == 0
    }

    override fun setLayoutId(): Int = R.layout.fragment_category

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setupWebView()
        swipeRefreshContainer.setColorSchemeColors(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_bright,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_dark
        )
    }

    override fun initData(bundle: Bundle?) {

    }

    override fun initListener() {
        failedView.setOnClickListener {
            bindData()
        }

        swipeRefreshContainer.setOnRefreshListener {
            bindData()
        }

        webHome.viewTreeObserver.addOnScrollChangedListener(this)
    }

    override fun bindData() {
        failedView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        webHome.visibility = View.INVISIBLE
        /*NewsApplication.getApiService().getUrl("navigation.html").enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                loadingView.visibility = View.GONE
                failedView.visibility = View.VISIBLE

                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
                swipeRefreshContainer.isRefreshing = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        webHome.loadDataWithBaseURL(null, response.body(), "text/html", "UTF-8", null)
                    } else {
                        failedView.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        swipeRefreshContainer.isRefreshing = false
                    }
                } else {
                    failedView.visibility = View.VISIBLE
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    swipeRefreshContainer.isRefreshing = false
                }
                loadingView.visibility = View.GONE
            }
        })*/
        webHome!!.loadUrl("https://dothocam.000webhostapp.com/account/navigation.html")
        loadingView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("JavascriptInterface")
    private fun setupWebView() {
        val webSetting = webHome.settings
        webSetting.javaScriptEnabled = true
        webHome.webChromeClient = HomeChromeClient()
        webHome.webViewClient = webViewClient
    }

    private fun goToDetail(url: String) {
        val bundle = Bundle()
        bundle.putString("detailUrl", url)
        if (baseActivity.supportFragmentManager.backStackEntryCount > 0) {
            baseActivity.onBackPressed()
        }
//        pushFragment(R.id.homeContainer, DetailFragment(), bundle, true)
    }

    private inner class HomeChromeClient : WebChromeClient() {
        override fun onJsAlert(view: WebView?, url: String?, message: String, result: JsResult): Boolean {
            result.confirm()
            goToDetail(message)
            return true
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (progress_bar != null)
                progress_bar.progress = newProgress
            super.onProgressChanged(view, newProgress)
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            if (progress_bar != null)
                progress_bar.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Handler().postDelayed({
                if (progress_bar != null)
                    progress_bar.visibility = View.GONE
                if (webHome != null)
                    webHome.visibility = View.VISIBLE
                if (shimmer_view_container != null) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                }
                if (swipeRefreshContainer != null){
                    swipeRefreshContainer.isRefreshing = false
                }
                super.onPageFinished(view, url)
            }, 500)
        }
    }
}
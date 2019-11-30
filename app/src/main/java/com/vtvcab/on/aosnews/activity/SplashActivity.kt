package com.vtvcab.on.aosnews.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vtvcab.on.aosnews.MainActivity
import com.vtvcab.on.aosnews.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupWebView()
        failedView.setOnClickListener {
            bindData()
        }
        bindData()
    }

    fun bindData() {
        failedView.visibility = View.GONE
        webHome.visibility = View.VISIBLE
       /* NewsApplication.getApiService().getUrl("index.html").enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                failedView.visibility = View.VISIBLE
                webHome.visibility = View.GONE
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        webHome.loadDataWithBaseURL(null, response.body(), "text/html", "UTF-8", null)
                    } else {
                        failedView.visibility = View.VISIBLE
                        webHome.visibility = View.GONE
                    }
                } else {
                    failedView.visibility = View.VISIBLE
                    webHome.visibility = View.GONE
                }
            }
        })*/
        webHome!!.loadUrl("https://dothocam.000webhostapp.com/")
    }

    @SuppressLint("JavascriptInterface")
    private fun setupWebView() {
        val webSetting = webHome.settings
        webSetting.javaScriptEnabled = true
        webHome.webChromeClient = HomeChromeClient()
        webHome.webViewClient = webViewClient
    }

    private fun goToDetail(url: String) {
     /*   val bundle = Bundle()
        bundle.putString("splashUrl", url)
        if (this.supportFragmentManager.backStackEntryCount > 0) {
            this.onBackPressed()
        }
        pushFragment(R.id.homeContainer, DetailFragment(), bundle, true)*/
        if (url == "Login VTV"){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private inner class HomeChromeClient : WebChromeClient() {
        override fun onJsAlert(view: WebView?, url: String?, message: String, result: JsResult): Boolean {
            result.confirm()
            Log.d("TAG", "url" + url)
            Log.d("TAG", "message" + message)
            goToDetail(message)
            return true
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            Handler().postDelayed({

                if (webHome != null)
                    webHome.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }, 500)
        }
    }
}
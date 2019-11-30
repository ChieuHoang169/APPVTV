package com.vtvcab.on.aosnews.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.vtvcab.on.aosnews.network.ApiService
import com.vtvcab.on.aosnews.network.ApiUtil

@SuppressLint("Registered")
class NewsApplication : Application() {
    companion object {
        private lateinit var sApiService : ApiService

        fun getApiService() : ApiService {
            return sApiService
        }
    }

    override fun onCreate() {
        super.onCreate()
        sApiService = ApiUtil.getApiService()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
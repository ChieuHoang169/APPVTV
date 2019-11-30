package com.vtvcab.on.aosnews.network

class ApiUtil {
    companion object {
        const val HOME_URL = "/"

        private val BASE_URL = "https://dothocam.000webhostapp.com"

        fun getApiService(): ApiService {
            return RDClient.getClient(BASE_URL).create(ApiService::class.java)
        }
    }
}
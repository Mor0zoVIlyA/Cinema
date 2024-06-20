package com.example.network

import okhttp3.Interceptor
import okhttp3.Response

class AppHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("X-API-KEY", "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
            .header("Content-Type", "application/json")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
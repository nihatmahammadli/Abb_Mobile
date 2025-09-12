package com.nihatmahammadli.abbmobile.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetry: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var tryCount = 0
        var lastException: IOException? = null

        while (tryCount < maxRetry) {
            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                lastException = e
            }
            tryCount++
        }
        throw lastException ?: IOException("RetryInterceptor: Failed after $maxRetry retries")
    }

}
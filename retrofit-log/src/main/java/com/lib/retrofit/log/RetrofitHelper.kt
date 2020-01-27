package com.lib.retrofit.log

import com.lib.db.ApiHelper
import com.lib.db.dto.Api
import com.lib.db.dto.ApiHeader
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.*

object RetrofitHelper {

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            null
        }
    }

    suspend fun insert(
        request: Request,
        successResponse: String? = null,
        response: Response? = null
    ) {
        val mutableListOfApiHeaders: MutableList<ApiHeader> = ArrayList()
        for (name in request.headers().names()) {
            mutableListOfApiHeaders.add(ApiHeader(name = name, value = request.headers()[name]))
        }
        ApiHelper.apiHelperInstance?.insert(
            Api(
                url = request.url().toString(),
                method = request.method(),
                requestBody = bodyToString(request),
                responseBody = successResponse,
                responseCode = response?.code() ?: -1,
                receivedResponseAtMillis = response?.receivedResponseAtMillis() ?: -1,
                sentRequestTime = response?.sentRequestAtMillis() ?: -1,
                createdDateTime = Date().time
            ), mutableListOfApiHeaders
        )
    }

    suspend fun insert(request: Request, errorCode: Int, errorMessage: String?) {
        val mutableListOfApiHeaders: MutableList<ApiHeader> = ArrayList()
        for (name in request.headers().names()) {
            mutableListOfApiHeaders.add(ApiHeader(name = name, value = request.headers()[name]))
        }
        ApiHelper.apiHelperInstance?.insert(
            Api(
                url = request.url().toString(),
                method = request.method(),
                requestBody = bodyToString(request),
                responseBody = errorMessage,
                responseCode = errorCode,
                receivedResponseAtMillis = -1,
                sentRequestTime = -1,
                createdDateTime = Date().time
            ), mutableListOfApiHeaders
        )
    }

}

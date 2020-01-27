package com.lib.retrofit.log

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.lib.db.dto.Api.Companion.NO_INTERNET
import com.lib.db.dto.Api.Companion.OTHER_ERROR
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException


class RetrofitCallback<T>(private val context: Context, private val mCallback: Callback<T>) :
    Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {

        GlobalScope.launch {
            RetrofitHelper.insert(
                call.request(),
                response.body().toString(),
                response.raw()
            )
        }

        mCallback.onResponse(call, response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {

        val errorCode: Int = if (!isNetworkConnected() && t is ConnectException) NO_INTERNET
        else OTHER_ERROR

        GlobalScope.launch {
            RetrofitHelper.insert(call.request(), errorCode, t.localizedMessage)
        }
        mCallback.onFailure(call, t)
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )
                }
            }
        }
        return false
    }
}
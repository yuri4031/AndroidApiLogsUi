package com.lib.db

import android.app.Application
import com.lib.db.db.ApiDatabase
import com.lib.db.db.ApiRepository
import com.lib.db.dto.Api
import com.lib.db.dto.ApiHeader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async


class ApiHelper private constructor(
    application: Application,
    private val showLog: Boolean,
    maxRecords: Int
) {
    private var repository: ApiRepository? = null

    init {
        if (showLog) {
            val wordsDao = ApiDatabase.getDatabase(application, MainScope(), maxRecords).apiDao()
            repository = ApiRepository(wordsDao)
        }
    }

    suspend fun insert(api: Api, mutableListOfApiHeaders: List<ApiHeader>) {
        if (showLog) {
            repository?.insert(api, mutableListOfApiHeaders)
        }
    }

//    fun insertAsync(api: Api, mutableListOfApiHeaders: List<ApiHeader>) =
//        GlobalScope.async { insert(api, mutableListOfApiHeaders) }

    companion object {
        var apiHelperInstance: ApiHelper? = null
            private set

        fun initialize(application: Application, showLog: Boolean, maxRecords: Int) {
            if (apiHelperInstance == null) {
                synchronized(ApiHelper::class.java) {
                    if (apiHelperInstance == null) {
                        apiHelperInstance =
                            ApiHelper(application, showLog, maxRecords)
                    }
                }
            }
        }

    }
}
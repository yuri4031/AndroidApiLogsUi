package com.lib.db.db

import androidx.lifecycle.LiveData
import com.lib.db.dto.Api
import com.lib.db.dto.ApiBodyAndHeader
import com.lib.db.dto.ApiHeader

class ApiRepository(private val apiDao: ApiDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allApis: LiveData<List<Api>> = apiDao.getAllApis()

    private suspend fun insert(api: Api): Long {
        return apiDao.insertApi(api)
    }

    fun findById(id: Long): LiveData<ApiBodyAndHeader?> {
        return apiDao.findById(id)
    }

    private suspend fun insert(mutableListOfApiHeaders: List<ApiHeader>) {
        apiDao.insertAllHeaders(mutableListOfApiHeaders)
    }

    suspend fun insert(api: Api, mutableListOfApiHeaders: List<ApiHeader>) {
        val apiId = insert(api)
        for (apiHeader in mutableListOfApiHeaders) {
            apiHeader.apiId = apiId
        }
        insert(mutableListOfApiHeaders)
    }
}
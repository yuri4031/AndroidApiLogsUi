package com.lib.db.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.lib.db.dto.Api
import com.lib.db.dto.ApiBodyAndHeader
import com.lib.db.dto.ApiHeader


@Dao
interface ApiDao {

    @Transaction
    @Query("SELECT * FROM api WHERE apiId=:id")
    fun findById(id: Long): LiveData<ApiBodyAndHeader?>

    @Transaction
    @Query("SELECT * FROM api")
    fun getApiWithBodyAndHeaders(): LiveData<List<ApiBodyAndHeader>>

    @Query("SELECT * FROM api_header")
    fun getAllHeaders(): LiveData<List<ApiHeader>>

    @Query("SELECT * FROM api order by apiId desc")
    fun getAllApis(): LiveData<List<Api>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApi(api: Api?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHeaders(apiHeader: List<ApiHeader>?)

    @Delete
    suspend fun delete(api: Api?): Int

    @RawQuery(observedEntities = [Api::class, ApiHeader::class])
    suspend fun deleteData(query: SupportSQLiteQuery?): Long

}

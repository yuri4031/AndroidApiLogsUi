package com.lib.db.db

import android.content.Context
import androidx.room.Room


class DatabaseClient private constructor(mCtx: Context) {

    private val appDatabase: ApiDatabase = Room.databaseBuilder(mCtx, ApiDatabase::class.java, "api_db").build()

    fun getAppDatabase(): ApiDatabase {
        return appDatabase
    }

    companion object {
        private var mInstance: DatabaseClient? = null
        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient? {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance
        }
    }

}

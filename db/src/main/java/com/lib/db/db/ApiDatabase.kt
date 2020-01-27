package com.lib.db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lib.db.dto.Api
import com.lib.db.dto.ApiHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(entities = [Api::class, ApiHeader::class], exportSchema = false, version = 1)
abstract class ApiDatabase : RoomDatabase() {
    abstract fun apiDao(): ApiDao

    private class ApiDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.apiDao().deleteData(
                        SimpleSQLiteQuery(
                            String.format(
                                Locale.getDefault(),
                                "DELETE FROM api where apiId NOT IN (SELECT apiId FROM api ORDER BY apiId DESC LIMIT %d)",
                                mMaxRecord
                            )
                        )
                    )
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.

//        private val NUMBER_OF_THREADS = 4
//        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Volatile
        private var INSTANCE: ApiDatabase? = null
        var mMaxRecord: Int = 100
        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
            maxRecord: Int = mMaxRecord
        ): ApiDatabase {

            this.mMaxRecord = maxRecord
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApiDatabase::class.java,
                    "api_db"
                ).addCallback(ApiDatabaseCallback(scope = scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
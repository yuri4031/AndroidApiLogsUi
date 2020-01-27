package com.lib.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lib.db.db.ApiDatabase
import com.lib.db.db.ApiRepository
import com.lib.db.dto.Api
import com.lib.db.dto.ApiHeader
import kotlinx.coroutines.launch
import java.util.*

// Class extends AndroidViewModel and requires application as a parameter.
class ApiListViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: ApiRepository
    // LiveData gives us updated words when they change.
    val allApis: LiveData<List<Api>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val wordsDao = ApiDatabase.getDatabase(application, viewModelScope).apiDao()
        repository = ApiRepository(wordsDao)
        allApis = repository.allApis
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    private fun insert(api: Api, mutableListOfApiHeaders: List<ApiHeader>) = viewModelScope.launch {
        repository.insert(api, mutableListOfApiHeaders)
    }

    fun insertDummy() {

        val headers = mutableListOf<ApiHeader>()
        headers.add(
            ApiHeader(
                name = "Content-Type",
                value = "application/json"
            )
        )

        insert(
            Api(
                url = "https://eps-net-uat.sadadbh.com/api/v2/mob-cus-sdd/users/login",
                method = "POST",
                requestBody = "{\n" +
                        "    \"app-platform\": \"Android\",\n" +
                        "    \"app-version\": \"3.4.0\",\n" +
                        "    \"device-id\": \"4:b1:67:3:f5:bf\",\n" +
                        "    \"msisdn\": \"97314141414\",\n" +
                        "    \"pin\": \"1234\",\n" +
                        "    \"app-push-token\": \"dqU_Aov9GHA:APA91bGvRF2auZEMqGIDjo-rIYJbzAoDP5RWmHVSrCap_Yxv_tRoR2VNdQyNLw1xIuIkDX3G27du8H3vCWM0xVrKf533Z-44sjgG28KS1vveSjSOTDNnFXhkKkyP4G8Kvwp2kYyhMDS8\"\n" +
                        "}",
                responseBody = "{\n" +
                        "    \"session-id\": \"9faf4731-f3cf-414a-a725-8910b2fd6ca6\",\n" +
                        "    \"update-version\": 0,\n" +
                        "    \"error-code\": 0\n" +
                        "}",
                responseCode = 200,
                receivedResponseAtMillis = 5L,
                sentRequestTime = 3L,
                createdDateTime = Date().time
            ), headers
        )
    }
}

package com.lib.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lib.db.db.ApiDatabase
import com.lib.db.db.ApiRepository
import com.lib.db.dto.Api
import com.lib.db.dto.ApiBodyAndHeader
import com.lib.db.dto.ApiHeader
import kotlinx.coroutines.launch
import java.util.*

// Class extends AndroidViewModel and requires application as a parameter.
class ApiDetailViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: ApiRepository
    // LiveData gives us updated words when they change.

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val wordsDao = ApiDatabase.getDatabase(application, viewModelScope).apiDao()
        repository = ApiRepository(wordsDao)
    }

    fun findById(id: Long): LiveData<ApiBodyAndHeader?> {
        return repository.findById(id)
    }
}

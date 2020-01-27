package com.lib.db.dto

import androidx.room.Embedded
import androidx.room.Relation

data class ApiBodyAndHeader(
    @Embedded
    var api: Api? = null,

    @Relation(parentColumn = "apiId", entityColumn = "apiId")
    var apiHeaderList: List<ApiHeader>? = null

){

    override fun toString(): String {
        return "ApiBodyAndHeader(api=$api, apiHeaderList=$apiHeaderList)"
    }
}

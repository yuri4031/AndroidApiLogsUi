package com.lib.db.dto

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.room.*
import java.io.Serializable
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*


@Entity(tableName = "api")
data class Api(

    @PrimaryKey(autoGenerate = true)
    var apiId: Long? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "method")
    var method: String? = null,

    @ColumnInfo(name = "request_body")
    var requestBody: String? = null,

    @ColumnInfo(name = "response_body")
    var responseBody: String? = null,

    @ColumnInfo(name = "response_code")
    var responseCode: Int = 0,

    @ColumnInfo(name = "received_response_time")
    var receivedResponseAtMillis: Long = 0,

    @ColumnInfo(name = "sent_request_time")
    var sentRequestTime: Long = 0,

    @ColumnInfo(name = "created_date")
    var createdDateTime: Long? = null,

    @Ignore
    var apiHeaderList: List<ApiHeader>? = null,

    @Ignore
    var checked: Boolean = false

) : Serializable {

    companion object {
        const val NO_INTERNET = 1000
        const val OTHER_ERROR = 999
    }

    @Ignore
    constructor(
        url: String?, method: String?, bodyToString: String?,
        response: String?, code: Int, receivedResponseAtMillis: Long, sentRequestAtMillis: Long,
        createdDateTime: Long?
    ) :
            this(
                url = url,
                method = method,
                requestBody = bodyToString,
                responseBody = response,
                responseCode = code,
                receivedResponseAtMillis = receivedResponseAtMillis,
                sentRequestTime = sentRequestAtMillis,
                createdDateTime = createdDateTime
            )


    fun getMethodColor(): Int {
        return when (method) {
            "POST" -> Color.parseColor("#ffc107") //yellow
            "GET" -> Color.parseColor("#4caf50") //green
            "PUT" -> Color.parseColor("#2196f3") //blue
            "DELETE" -> Color.parseColor("#f44336") //red
            else -> Color.parseColor("#bdbdbd") //grey
        }
    }

    fun getCodeColor(): Int {
        return when (responseCode) {
            in 200..300 -> Color.parseColor("#4caf50") //green
            in 300..400 -> Color.parseColor("#ffc107") //yellow
            in 400..500 -> Color.parseColor("#2196f3") //blue
            else -> Color.parseColor("#f44336") //red
        }
    }

    override fun toString(): String {
        return "Api(apiId=$apiId, url=$url, method=$method, requestBody=$requestBody, responseBody=$responseBody, responseCode=$responseCode, receivedResponseAtMillis=$receivedResponseAtMillis, sentRequestTime=$sentRequestTime, createdDateTime=${getCreatedAt()}, apiHeaderList=$apiHeaderList, checked=$checked)"
    }

    @SuppressLint("SimpleDateFormat")
    fun getCreatedAt(): String? {
        return createdDateTime?.let { SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(Date(it)) }
    }

    fun getTtl(): String? {
        return if (receivedResponseAtMillis != -1L && sentRequestTime != -1L)
            "(" + (receivedResponseAtMillis - sentRequestTime).toString() + " ms)"
        else null
    }

    fun hasErrorCode(): Boolean {
        return !(responseCode == -1 || responseCode == NO_INTERNET || responseCode == OTHER_ERROR)
    }

}

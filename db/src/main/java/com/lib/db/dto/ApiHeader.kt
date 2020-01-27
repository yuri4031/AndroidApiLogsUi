package com.lib.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "api_header",
    foreignKeys = [
        ForeignKey(
            entity = Api::class,
            parentColumns = ["apiId"],
            childColumns = ["apiId"],
            onDelete = CASCADE
        )
    ]
)

data class ApiHeader(

    @PrimaryKey(autoGenerate = true)
    var headerId: Long? = null,

    @ColumnInfo(name = "apiId")
    var apiId: Long? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "value")
    var value: String? = null
) : Serializable {

    override fun toString(): String {
        return "ApiHeader(headerId=$headerId, apiId=$apiId, name=$name, value=$value)"
    }


}

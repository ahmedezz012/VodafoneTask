package com.ezz.vodafonetask.data.models.dto


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ezz.vodafonetask.utils.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
@Entity(tableName = Constants.Database.TableNames.PHOTOS_TABLE_NAME)
data class PhotoItem(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("download_url")
    val downloadUrl: String? = null
) : Parcelable {

}
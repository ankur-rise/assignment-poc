package com.llm.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Keep @Entity @Parcelize
data class DeliveryItemDataModel constructor(
    @PrimaryKey val id:Int, val description:String, @ColumnInfo(name = "image_url") val imageUrl:String,
    @Embedded val location:LatLongDataModel
) : Parcelable
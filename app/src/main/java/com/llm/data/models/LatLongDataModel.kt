package com.llm.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize @Keep
data class LatLongDataModel constructor(val lat:Double, val lng:Double, val address:String):Parcelable
package com.llm

import com.llm.data.models.DeliveryItemDataModel
import com.llm.data.models.LatLongDataModel

fun getDummyDeliveryItem(): DeliveryItemDataModel {
    return DeliveryItemDataModel(0, "", "", LatLongDataModel(0.0, 0.0, ""))
}
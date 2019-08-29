package com.llm.ui.utils

import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.llm.data.models.LatLongDataModel

@BindingAdapter("app:uri")
fun setImageUri(view: SimpleDraweeView, uri:String?) {

    view.setImageURI(uri?:"")

}

@BindingAdapter("app:coordinates")
fun configureMapVariables(view:MapView, latLng:LatLongDataModel?){
    view.getMapAsync { map->

        if(latLng!=null) {
            val location = LatLng(latLng.lat, latLng.lng)
            map.addMarker(MarkerOptions().position(location).title(latLng.address))
            map.moveCamera(CameraUpdateFactory.newLatLng(location))
        }
    }
}
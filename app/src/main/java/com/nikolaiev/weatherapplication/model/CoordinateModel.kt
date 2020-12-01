package com.nikolaiev.weatherapplication.model

import android.os.Parcel
import android.os.Parcelable

class CoordinateModel() : Parcelable {
    var lat:Double=0.0
    var lon:Double=0.0

    constructor(parcel: Parcel) : this() {
        lat = parcel.readDouble()
        lon = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoordinateModel> {
        override fun createFromParcel(parcel: Parcel): CoordinateModel {
            return CoordinateModel(parcel)
        }

        override fun newArray(size: Int): Array<CoordinateModel?> {
            return arrayOfNulls(size)
        }
    }
}
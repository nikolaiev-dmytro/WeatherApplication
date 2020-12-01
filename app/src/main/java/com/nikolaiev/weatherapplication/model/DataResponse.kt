package com.nikolaiev.weatherapplication.model

import android.os.Parcel
import android.os.Parcelable

class DataResponse() : Parcelable {
    var message: String? = null
    var cod: Int = 0
    var count: Int = 0
    var list: List<CityModel>? = emptyList()

    constructor(parcel: Parcel) : this() {
        message = parcel.readString()
        cod = parcel.readInt()
        count = parcel.readInt()
        list = parcel.createTypedArrayList(CityModel)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeInt(cod)
        parcel.writeInt(count)
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataResponse> {
        override fun createFromParcel(parcel: Parcel): DataResponse {
            return DataResponse(parcel)
        }

        override fun newArray(size: Int): Array<DataResponse?> {
            return arrayOfNulls(size)
        }
    }
}
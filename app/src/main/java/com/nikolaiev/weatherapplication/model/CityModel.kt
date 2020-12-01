package com.nikolaiev.weatherapplication.model

import android.os.Parcel
import android.os.Parcelable

class CityModel() : Parcelable {

    var id: Long = 0
    var name: String? = null
    var state: String? = null
    var country: String? = null
    var coord: CoordinateModel? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        state = parcel.readString()
        country = parcel.readString()
        coord = parcel.readParcelable(CoordinateModel::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeParcelable(coord, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityModel> {
        override fun createFromParcel(parcel: Parcel): CityModel {
            return CityModel(parcel)
        }

        override fun newArray(size: Int): Array<CityModel?> {
            return arrayOfNulls(size)
        }
    }
}
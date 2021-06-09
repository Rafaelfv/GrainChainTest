package com.rafaelfv.grainchaintest.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class RouteInfo(
    @PrimaryKey(autoGenerate = true) val id: Long? = -1,
    val title: String
): Parcelable

@Parcelize
@Entity
data class Dot(
    @PrimaryKey (autoGenerate = true) val id: Long? = -1,
    val latitude: Double,
    val longitude: Double,
    val routeId: Long
): Parcelable

@Parcelize
data class Route(
    @Embedded val routeInfo: RouteInfo,
    @Relation(parentColumn = "id", entityColumn = "routeId")
    val dots: List<Dot>
):Parcelable
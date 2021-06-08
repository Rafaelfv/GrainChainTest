package com.rafaelfv.grainchaintest.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class RouteInfo(
    @PrimaryKey(autoGenerate = true) val id: Long? = -1,
    val title: String
)

@Entity
data class Dot(
    @PrimaryKey (autoGenerate = true) val id: Long? = -1,
    val latitude: Double,
    val longitude: Double,
    val routeId: Long
)

data class Route(
    @Embedded val routeInfo: RouteInfo,
    @Relation(parentColumn = "id", entityColumn = "routeId")
    val dots: List<Dot>
)
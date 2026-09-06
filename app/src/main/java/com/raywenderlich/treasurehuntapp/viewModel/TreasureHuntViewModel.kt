package com.raywenderlich.treasurehuntapp.viewModel

import androidx.lifecycle.ViewModel
import com.raywenderlich.treasurehuntapp.data.TreasureLocations
import com.raywenderlich.treasurehuntapp.model.TreasureLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TreasureHuntViewModel : ViewModel() {

    private val _currentLocationIndex = MutableStateFlow(0)

    val currentLocationIndex: StateFlow<Int> =
        _currentLocationIndex.asStateFlow()

    private val _completedCount = MutableStateFlow(0)

    val completedCount: StateFlow<Int> =
        _completedCount.asStateFlow()

    private val _isHuntComplete = MutableStateFlow(false)

    val isHuntComplete: StateFlow<Boolean> =
        _isHuntComplete.asStateFlow()

    private val _userLatitude = MutableStateFlow<Double?>(null)

    val userLatitude: StateFlow<Double?> =
        _userLatitude.asStateFlow()

    private val _userLongitude = MutableStateFlow<Double?>(null)

    val userLongitude: StateFlow<Double?> =
        _userLongitude.asStateFlow()

    private val _distanceToTarget = MutableStateFlow<Double?>(null)

    val distanceToTarget: StateFlow<Double?> =
        _distanceToTarget.asStateFlow()

    val currentLocation: TreasureLocation
        get() = TreasureLocations.locations[_currentLocationIndex.value]

    fun updateUserLocation(
        latitude: Double,
        longitude: Double
    ) {
        _userLatitude.value = latitude
        _userLongitude.value = longitude

        val target = currentLocation

        val distance = calculateDistance(
            latitude,
            longitude,
            target.latitude,
            target.longitude
        )

        _distanceToTarget.value = distance
    }

    fun checkCurrentLocation(): Boolean {

        val distance = _distanceToTarget.value
            ?: return false

        val allowedDistance = 100.0

        if (distance <= allowedDistance) {

            if (_completedCount.value < TreasureLocations.locations.size) {

                _completedCount.value++

                if (_completedCount.value >= TreasureLocations.locations.size) {

                    _isHuntComplete.value = true

                } else {

                    _currentLocationIndex.value++
                }
            }

            return true
        }

        return false
    }

    private fun calculateDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): Double {

        val earthRadius = 6371000.0

        val latitudeDifference =
            Math.toRadians(endLatitude - startLatitude)

        val longitudeDifference =
            Math.toRadians(endLongitude - startLongitude)

        val a =
            sin(latitudeDifference / 2).pow(2) +
                    cos(Math.toRadians(startLatitude)) *
                    cos(Math.toRadians(endLatitude)) *
                    sin(longitudeDifference / 2).pow(2)

        val c =
            2 * atan2(
                sqrt(a),
                sqrt(1 - a)
            )

        return earthRadius * c
    }
}
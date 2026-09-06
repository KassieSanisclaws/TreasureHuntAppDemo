package com.raywenderlich.treasurehuntapp.model

/**
 * Represents one location in the treasure hunt.
 *
 * @property id The position of this location in the hunt.
 * @property name The name of the location.
 * @property address The street address.
 * @property latitude Geographic latitude.
 * @property longitude Geographic longitude.
 * @property clue The clue displayed to the player.
 */
data class TreasureLocation(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val clue: String
)
package com.raywenderlich.treasurehuntapp.ui.theme.appUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.raywenderlich.treasurehuntapp.viewModel.TreasureHuntViewModel
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@SuppressLint("MissingPermission")
@Composable
fun HuntScreen(
    viewModel: TreasureHuntViewModel,
    onHuntComplete: () -> Unit
) {

    val context = LocalContext.current

    val currentLocation by remember {
        derivedStateOf {
            viewModel.currentLocation
        }
    }

    val completedCount by viewModel.completedCount.collectAsState()

    val userLatitude by viewModel.userLatitude.collectAsState()

    val userLongitude by viewModel.userLongitude.collectAsState()

    val distanceToTarget by viewModel.distanceToTarget.collectAsState()

    val isHuntComplete by viewModel.isHuntComplete.collectAsState()

    var showPermissionMessage by remember {
        mutableStateOf(false)
    }

    val targetLatLng = LatLng(
        currentLocation.latitude,
        currentLocation.longitude
    )

    val cameraPositionState = rememberCameraPositionState {

        position = CameraPosition.fromLatLngZoom(
            targetLatLng,
            14f
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                    ?: false

            val coarseGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION]
                    ?: false

            if (!fineGranted && !coarseGranted) {

                showPermissionMessage = true
            }
        }

    fun hasLocationPermission(): Boolean {

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    fun requestLocation() {

        if (!hasLocationPermission()) {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            return
        }

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient
            .getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
            .addOnSuccessListener { location ->

                if (location != null) {

                    viewModel.updateUserLocation(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }
            }
    }

    LaunchedEffect(Unit) {

        requestLocation()
    }

    LaunchedEffect(isHuntComplete) {

        if (isHuntComplete) {

            onHuntComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Treasure Hunt",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Progress: $completedCount / 20"
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = currentLocation.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = currentLocation.address,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = currentLocation.clue,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (distanceToTarget != null) {

                    Text(
                        text = String.format(
                            "Distance: %.0f metres",
                            distanceToTarget
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission()
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {

            Marker(
                state = MarkerState(position = targetLatLng),
                title = currentLocation.name,
                snippet = currentLocation.address
            )

            if (
                userLatitude != null &&
                userLongitude != null
            ) {

                val userLatLng = LatLng(
                    userLatitude!!,
                    userLongitude!!
                )

                Marker(
                    state = MarkerState(position = userLatLng),
                    title = "Your Location"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    requestLocation()
                },
                modifier = Modifier.weight(1f)
            ) {

                Text("Update Location")
            }

            Button(
                onClick = {

                    val reached =
                        viewModel.checkCurrentLocation()

                    if (!reached) {
                        showPermissionMessage = true
                    }

                },
                modifier = Modifier.weight(1f)
            ) {

                Text("Check Location")
            }
        }
    }

    if (showPermissionMessage) {

        AlertDialog(
            onDismissRequest = {
                showPermissionMessage = false
            },
            title = {
                Text("Location Required")
            },
            text = {
                Text(
                    "Please allow location access and make sure you are close to the current treasure hunt destination."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        showPermissionMessage = false
                    }
                ) {

                    Text("OK")
                }
            }
        )
    }
}
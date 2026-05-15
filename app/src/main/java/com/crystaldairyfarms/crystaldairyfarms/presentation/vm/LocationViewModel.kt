package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

@HiltViewModel
class LocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _locationText = MutableStateFlow("Locating...")
    val locationText: StateFlow<String> = _locationText.asStateFlow()

    fun fetchLocation() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            _locationText.value = "New Delhi, IND"
            return
        }

        viewModelScope.launch {
            try {
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                val cancellationToken = CancellationTokenSource()
                val location = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationToken.token
                ).await()

                if (location != null) {
                    resolveAddress(location.latitude, location.longitude)
                } else {
                    // Fall back to last known location
                    val last = fusedClient.lastLocation.await()
                    if (last != null) resolveAddress(last.latitude, last.longitude)
                    else _locationText.value = "Location unavailable"
                }
            } catch (e: Exception) {
                _locationText.value = "New Delhi, IND"
            }
        }
    }

    private fun resolveAddress(lat: Double, lon: Double) {
        try {
            @Suppress("DEPRECATION")
            val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(lat, lon, 1)
            val addr = addresses?.firstOrNull()
            val locality = addr?.locality ?: addr?.subLocality ?: addr?.subAdminArea ?: ""
            val admin = addr?.adminArea ?: ""
            _locationText.value = when {
                locality.isNotEmpty() && admin.isNotEmpty() -> "$locality, $admin"
                locality.isNotEmpty() -> locality
                admin.isNotEmpty() -> admin
                else -> "Location found"
            }
        } catch (e: Exception) {
            _locationText.value = "New Delhi, IND"
        }
    }
}
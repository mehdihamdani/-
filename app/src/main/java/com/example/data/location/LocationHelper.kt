package com.example.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.data.model.UserLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(
        onSuccess: (UserLocation) -> Unit,
        onError: (() -> Unit)? = null
    ) {
        if (!hasLocationPermission()) {
            onError?.invoke()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                if (loc != null && isValidCoord(loc)) {
                    val city = resolveCityName(loc.latitude, loc.longitude)
                    onSuccess(UserLocation(loc.latitude, loc.longitude, city))
                } else {
                    onError?.invoke()
                }
            }
            .addOnFailureListener {
                onError?.invoke()
            }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationHighAccuracy(
        onSuccess: (UserLocation) -> Unit,
        onError: ((Exception?) -> Unit)? = null
    ) {
        if (!hasLocationPermission()) {
            onError?.invoke(SecurityException("Location permission not granted"))
            return
        }

        val cts = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { loc: Location? ->
                if (loc != null && isValidCoord(loc)) {
                    val city = resolveCityName(loc.latitude, loc.longitude)
                    onSuccess(UserLocation(loc.latitude, loc.longitude, city))
                } else {
                    // Fallback to last known location
                    getLastKnownLocation(
                        onSuccess = onSuccess,
                        onError = { onError?.invoke(null) }
                    )
                }
            }
            .addOnFailureListener { e ->
                onError?.invoke(e)
            }
    }

    @SuppressLint("MissingPermission")
    fun getLocationUpdatesFlow(intervalMs: Long = 10000L): Flow<UserLocation> = callbackFlow {
        if (!hasLocationPermission()) {
            close()
            return@callbackFlow
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, intervalMs)
            .setMinUpdateDistanceMeters(15f)
            .setMinUpdateIntervalMillis(5000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    if (isValidCoord(loc)) {
                        val city = resolveCityName(loc.latitude, loc.longitude)
                        trySend(UserLocation(loc.latitude, loc.longitude, city))
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    private fun isValidCoord(loc: Location): Boolean {
        return loc.latitude != 0.0 && loc.longitude != 0.0
    }

    fun resolveCityName(lat: Double, lng: Double): String {
        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale("ar", "DZ"))
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    val locality = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                    if (!locality.isNullOrBlank()) {
                        return locality
                    }
                }
            }
        } catch (_: Exception) {}

        return matchAlgerianWilaya(lat, lng)
    }

    private fun matchAlgerianWilaya(lat: Double, lng: Double): String {
        val wilayas = listOf(
            Triple("الجزائر العاصمة", 36.7538, 3.0588),
            Triple("البليدة", 36.4700, 2.8300),
            Triple("بومرداس", 36.7667, 3.4667),
            Triple("تيبازة", 36.5925, 2.4475),
            Triple("وهران", 35.6987, -0.6345),
            Triple("قسنطينة", 36.3498, 6.6192),
            Triple("سطيف", 36.1912, 5.4078),
            Triple("عنابة", 36.8870, 7.7450),
            Triple("تلمسان", 34.8783, -1.3150),
            Triple("باتنة", 35.5560, 6.1741),
            Triple("بجاية", 36.7559, 5.0843),
            Triple("تيزي وزو", 36.7118, 4.0459),
            Triple("الشلف", 36.1653, 1.3345),
            Triple("بسكرة", 34.8504, 5.7281),
            Triple("ورقلة", 31.9530, 5.3300),
            Triple("غرداية", 32.4909, 3.6735)
        )
        var closest = "موقعي الحالي"
        var minDist = Double.MAX_VALUE
        for ((name, wLat, wLng) in wilayas) {
            val d = Math.hypot(lat - wLat, lng - wLng)
            if (d < minDist) {
                minDist = d
                closest = name
            }
        }
        return if (minDist < 0.6) closest else "موقعي الحالي"
    }
}

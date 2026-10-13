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
import com.example.data.model.AlgerianWilayas
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
        var closest = "موقعي الحالي"
        var minDist = Double.MAX_VALUE
        for (w in AlgerianWilayas.all69Wilayas) {
            val d = Math.hypot(lat - w.latitude, lng - w.longitude)
            if (d < minDist) {
                minDist = d
                closest = "ولاية ${w.arabicName}"
            }
        }
        return if (minDist < 0.6) closest else "موقعي الحالي"
    }
}

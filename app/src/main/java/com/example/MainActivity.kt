package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.data.local.AppDatabase
import com.example.data.location.LocationHelper
import com.example.data.repository.RestroomRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RestroomViewModel
import com.example.ui.viewmodel.RestroomViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: RestroomViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        val repo = RestroomRepository(
            dao = db.restroomDao(),
            cachedLocationDao = db.cachedLocationDao(),
            cacheMetadataDao = db.offlineCacheMetadataDao()
        )
        RestroomViewModelFactory(repo)
    }

    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationHelper = LocationHelper(this)

        setContent {
            MyApplicationTheme {
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                    if (fineGranted || coarseGranted) {
                        detectUserLocation()
                        startLiveLocationUpdates()
                    }
                }

                LaunchedEffect(Unit) {
                    val fineCheck = ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    if (fineCheck != PackageManager.PERMISSION_GRANTED) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        detectUserLocation()
                        startLiveLocationUpdates()
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun detectUserLocation() {
        viewModel.requestGpsLocation(locationHelper)
    }

    private fun startLiveLocationUpdates() {
        lifecycleScope.launch {
            try {
                locationHelper.getLocationUpdatesFlow(intervalMs = 15000L).collect { loc ->
                    viewModel.setUserLocation(loc.latitude, loc.longitude, loc.cityName)
                }
            } catch (_: Exception) {
                // Ignore permission revocation during background
            }
        }
    }
}

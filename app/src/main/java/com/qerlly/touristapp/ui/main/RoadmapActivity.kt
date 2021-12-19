package com.qerlly.touristapp.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qerlly.touristapp.BuildConfig
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.ActivityRoadmapBinding
import com.qerlly.touristapp.model.MemberPoint
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.model.map.MyOwnItemizedOverlay
import com.qerlly.touristapp.ui.main.adapters.FaqListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.OverlayItem
import javax.inject.Inject

@AndroidEntryPoint
class RoadmapActivity : AppCompatActivity(), LocationListener {
    private val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION: Int = 1
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 0
    private val MIN_SEC: Long = 5L;//SECS
    private val MIN_DIST: Float = 3.0f;//METERS
    private val TAG = "MainActivity"
    private lateinit var navController: NavController
    lateinit var mgr: LocationManager
    private lateinit var criteria: Criteria
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityRoadmapBinding
    private var mapController: MapController? = null
    private var pts: List<TourPoint>? = ArrayList()
    private var members: List<MemberPoint>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoadmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = FaqListAdapter(viewModel::onCardClicked)
        val manager = LinearLayoutManager(this)
        binding?.points?.setLayoutManager(manager)
        binding?.points?.adapter = adapter

        viewModel.pointsNameDesc.onEach {
            if (it == null) {
                binding?.points?.visibility = View.GONE
            } else {
                binding.points?.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        /*val adapter = FaqListAdapter(viewModel::onCardClicked)
        val manager = LinearLayoutManager(requireContext())
        _bind?.points?.setLayoutManager(manager)
        _bind?.points?.adapter = adapter*/
        /*viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)*/
        /*setContentView(R.layout.activity_main)*/
        //initObservers() //todo send location to the viewModel
        viewModel.tourPoints.onEach {
            pts = it
            draw()
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.membersPoints.onEach {
            members = it
            draw()

        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        prepareLoc()
        //navController.navigate(R.id.userLocationsFragment)//todo remove after testing
    }

    fun draw(){
        binding?.mapView!!.overlays.clear()
        drawMembersOnMap()
        drawPointsOnMap()
    }

    private fun drawMembersOnMap(){
        members?.forEach{
            val icon = IconOverlay()
            var drawable: Drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_black)
            icon.set(GeoPoint(it.latitude.toDouble(), it.longitude.toDouble()), drawable)
            /*var drawable: Drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_black)
            if (it.){
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_red)
            } else if (it.ifSelfLoc) {
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_green)
            }*/
            binding?.mapView?.overlays?.add(icon)
            /*if (it.ifSelfLoc) {
                mapController!!.setCenter(GeoPoint(it.lat, it.long))
            }*/
        }
    }

    private fun initObservers() {
        /*viewModel.currentPointLatLong.observe(this, viewModel::sendCurrentLocation)*/
    }

    override fun onLocationChanged(location: Location) {
        /*viewModel.currentPointLatLong.value = Pair(location.latitude, location.longitude)*/
        viewModel.updateLocation(location.latitude.toString(), location.longitude.toString())
        //Log.d(TAG, location.latitude.toString() + " " + location.longitude)
    }

    private fun prepareLoc() {
        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController*/
        mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        setUpGPS()
        /*navController.navigate(R.id.navigation_roadmap)*/
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //draw()
        } else {
            requireGps()
        }
    }

    private fun drawPointsOnMap() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        binding!!.mapView.setBuiltInZoomControls(true)
        binding!!.mapView.setMultiTouchControls(true)
        mapController = binding!!.mapView.controller as MapController
        mapController!!.setZoom(15)

        pts?.forEach { point ->
            run {
                mapController?.setCenter(
                    GeoPoint(
                        point.latitude.toDouble(),
                        point.longitude.toDouble()
                    )
                )
                val itemizedOverlay = MyOwnItemizedOverlay(
                    this@RoadmapActivity,
                    listOf(OverlayItem(point.description, point.title, object : IGeoPoint {
                        override fun getLatitudeE6(): Int {
                            return 0;
                        }

                        override fun getLongitudeE6(): Int {
                            return 0;
                        }

                        override fun getLatitude(): Double {
                            return point.latitude.toDouble()
                        }

                        override fun getLongitude(): Double {
                            return point.longitude.toDouble()
                        }
                    }
                    )
                    )
                )
                binding?.mapView!!.overlays.add(itemizedOverlay)
                /*val icon1 = ItemizedIconOverlay(listOf(OverlayItem(point.textOnClosed, point.textOnClosed, object: IGeoPoint{
                    override fun getLatitude(): Double {
                        return point.lat
                    }
                    override fun getLongitude(): Double {
                        return point,
                    }
                })))//
                val icon = IconOverlay()
                icon.set(GeoPoint(point.latitude, point.longitude), resources.getDrawable(R.drawable.ic_baseline_location_on_24))
                _bind?.mapView!!.overlays.add(icon)*/
                //ItemizedIconOverlay
            }
        }
        //
        // mapController!!.setCenter(GeoPoint(pts.get(0).latitude.toDouble(), pts.get(0).longitude.toDouble()))
    }

    override fun onPause() {
        super.onPause()
        mgr.removeUpdates(this)
    }

    private fun setUpGPS() {
        criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val providers = mgr.getProviders(criteria, true)
        if (providers.size == 0) {
            Log.d(TAG, "Could not open GPS service")
            return
        }
        val preffered = providers.get(0)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mgr.requestLocationUpdates(preffered, MIN_SEC * 1000, MIN_DIST, this)
    }

    private fun requireGps() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setUpGPS()
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the background location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestBackgroundLocationPermission()
                    }
                    .create()
                    .show()

            }
        } else {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Now check background location
                        checkBackgroundLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        setUpGPS()
                        Toast.makeText(
                            this,
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "background locatin permission denied", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
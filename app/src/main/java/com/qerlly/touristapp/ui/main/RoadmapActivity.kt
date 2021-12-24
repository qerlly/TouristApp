package com.qerlly.touristapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.qerlly.touristapp.BuildConfig
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.ActivityRoadmapBinding
import com.qerlly.touristapp.model.MemberPoint
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.model.map.MyOwnItemizedOverlay
import com.qerlly.touristapp.services.UserAuthService
import com.qerlly.touristapp.ui.main.adapters.FaqListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.OverlayItem
import javax.inject.Inject
import androidx.appcompat.widget.Toolbar
import android.location.LocationListener
import android.location.LocationManager
import timber.log.Timber
import android.location.Criteria
import com.qerlly.touristapp.repositories.TourRepository
import org.slf4j.MarkerFactory.getMarker


@AndroidEntryPoint
class RoadmapActivity : AppCompatActivity(), LocationListener {

    @Inject
    lateinit var authService: UserAuthService

    @Inject
    lateinit var tourRepository: TourRepository

    private var locationManager: LocationManager? = null
    private var myLoc: Location? = null

    private val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION: Int = 1
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 0
    private val MIN_SEC: Long = 5L
    private val MIN_DIST: Float = 3.0f
    var mgr: LocationManager? = null
    private var criteria: Criteria? = null
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityRoadmapBinding
    private var mapController: MapController? = null
    private var pts: List<TourPoint>? = ArrayList()
    private var members: List<MemberPoint>? = ArrayList()

    private var activityInitiated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoadmapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        initLocManager()

        val adapter = FaqListAdapter(viewModel::onCardClicked)
        val manager = LinearLayoutManager(this)
        binding.points.layoutManager = manager
        binding.points.adapter = adapter

        mapController = binding.mapView.controller as MapController
        mapController!!.setZoom(15)

        viewModel.localizationState
            .filterNot { it }
            .onEach { Toast.makeText(this, R.string.no_localization, Toast.LENGTH_LONG).show() }
            .flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.pointsNameDesc.combine(viewModel.localizationState, ::Pair)
            .filter { it.second }
            .map { it.first }
            .onEach {
            if (it == null) {
                binding.points.visibility = View.GONE
            } else {
                binding.points.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.tourPoints.combine(viewModel.localizationState, ::Pair)
            .filter { it.second }
            .map { it.first }
            .onEach {
            pts = it
            draw()
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.membersPoints.combine(viewModel.localizationState, ::Pair)
            .filter { it.second }
            .map { it.first }
            .filterNotNull()
            .map { list ->
            list.filter { it.latitude.isNotEmpty() && it.longitude.isNotEmpty() }
        }.onEach {
            members = it
            draw()
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.localizationState
            .filter { it }
            .onEach { prepareLoc() }
            .flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
    }

    private fun initLocManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myLoc = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            myLoc?.let {
                viewModel.updateLocation(it.latitude.toString(), it.longitude.toString())
            }
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        }
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setTitle(R.string.roadmap)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun draw(){
        binding.mapView.overlays.clear()
        drawMembersOnMap()
        drawPointsOnMap()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawMembersOnMap(){
        members?.forEach{
            val icon = IconOverlay()
            var drawable: Drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_black)
            if (it.email.endsWith("@firma.gid.com")){
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_red)
            } else if (it.email == authService.userEmail) {
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_green)
            }
            icon.set(GeoPoint(it.latitude.toDouble(), it.longitude.toDouble()), drawable)
            binding.mapView.overlays?.add(icon)
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModel.updateLocation(location.latitude.toString(), location.longitude.toString())
    }

    private fun prepareLoc() {
        mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        setUpGPS()
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
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            draw()
        } else {
            requireGps()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawPointsOnMap() {
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setBuiltInZoomControls(true)
        binding.mapView.setMultiTouchControls(true)

        pts?.forEach { point ->
            if(!activityInitiated) {
                mapController!!.setCenter(
                    GeoPoint(
                        point.latitude.toDouble(),
                        point.longitude.toDouble()
                    )
                )
                activityInitiated = true
            }
            val item = OverlayItem("${point.id}. ${point.title}", "${point.description}\t${point.status}\t${authService.userEmail}", object : IGeoPoint {
                    override fun getLatitudeE6(): Int {
                        return 0
                    }

                    override fun getLongitudeE6(): Int {
                        return 0
                    }

                    override fun getLatitude(): Double {
                        return point.latitude.toDouble()
                    }

                    override fun getLongitude(): Double {
                        return point.longitude.toDouble()
                    }
                }
            )
            item.setMarker(getMarker(point))
            val itemizedOverlay = MyOwnItemizedOverlay(
                this@RoadmapActivity,
                tourRepository,
                point,
                listOf(item)
            )
            binding.mapView.overlays.add(itemizedOverlay)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getMarker(point: TourPoint): Drawable {
        val prefix = when (point.status) {
            2L -> "true"
            1L -> "current"
            else -> "false"
        }
        val resourceId = resources.getIdentifier("$prefix${point.id}", "drawable", this.packageName)
        return resources.getDrawable(resourceId)
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates((this as LocationListener))
        mgr?.removeUpdates(this)
    }

    private fun setUpGPS() {
        criteria = Criteria()
        criteria?.accuracy = Criteria.ACCURACY_FINE
        val providers = mgr?.getProviders(criteria!!, false)
        if (providers?.size == 0) return
        val preffered = providers?.get(0)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mgr?.requestLocationUpdates(preffered!!, MIN_SEC * 1000, MIN_DIST, this)
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
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
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the background location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkBackgroundLocation()
                    }

                } else {
                    Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show()
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(this, "background locatin permission denied", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }
}
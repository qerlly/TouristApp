package com.qerlly.touristapp.application.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.qerlly.touristapp.BuildConfig
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.MapPointsFragmentBinding
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline




class MapPointsFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var mapController: MapController
    private var binding: MapPointsFragmentBinding? = null
    companion object {
        fun newInstance() = MapPointsFragment()
    }

    private lateinit var viewModel: MapPointsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapPointsFragmentBinding.inflate(layoutInflater)
        prepareMap()
        return binding!!.root
    }

    private fun prepareMap(){
        mapView = binding!!.mapView
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller as MapController
        mapController.setZoom(19)
        org.osmdroid.config.Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        val location = (activity as MainActivity).mgr.getLastKnownLocation((activity as MainActivity).mgr.allProviders.get(0))
        if (location != null) {
            val icon = IconOverlay()
            icon.set(GeoPoint(location.latitude, location.longitude), resources.getDrawable(R.drawable.ic_baseline_location_on_24))

            mapController.setCenter(GeoPoint(location.latitude, location.longitude))
            val overlays = mutableListOf<OverlayItem>()
            overlays.add(OverlayItem("new Overlay", "OverlayDescription", GeoPoint(location.latitude, location.longitude)))
            val line = Polyline()
            line.width = 2f
            line.color = Color.BLACK
            val pts: MutableList<GeoPoint> = ArrayList()
            pts.add(GeoPoint(location.latitude, location.longitude))
            pts.add(GeoPoint(51.0988449, 17.0354319))
            line.setPoints(pts)
            line.isGeodesic = true
            mapView.overlays.add(line)
            mapView.overlays.add(icon)
            val polyline = Polyline(mapView, false)
            /*polyline.isGeodesic = false*/
            polyline.actualPoints.add(GeoPoint(51.0988449, 17.0354319))
            polyline.actualPoints.add(GeoPoint(location.latitude, location.longitude))
            polyline.color = Color.RED
            polyline.width = 2f
            polyline.isGeodesic = true
            mapView.overlays.add(polyline)

            /*val lineP = Polyline()
            line.width = 20f
            val pts: MutableList<GeoPoint> = ArrayList()
            pts.add(GeoPoint(40.796788, -73.949232))
            pts.add(GeoPoint(40.796788, -73.981762))
            line.setPoints(pts)
            line.isGeodesic = true
            mapView.getOverlayManager().add(line)*/
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapPointsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
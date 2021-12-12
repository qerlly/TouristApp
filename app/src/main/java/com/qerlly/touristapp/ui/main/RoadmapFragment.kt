package com.qerlly.touristapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.qerlly.touristapp.BuildConfig
import com.qerlly.touristapp.R
import com.qerlly.touristapp.application.main.viewmodels.RoadmapViewModel
import com.qerlly.touristapp.databinding.FragmentRoadmapBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.TilesOverlay

@AndroidEntryPoint
class RoadmapFragment : Fragment() {
    private var binding: FragmentRoadmapBinding? = null
    private lateinit var mapView: MapView
    private lateinit var mapController: MapController

    private val viewModel: RoadmapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoadmapBinding.inflate(layoutInflater)
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
        val mProvider = MapTileProviderBasic(requireContext());

//Tiles

        mProvider.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);



        val mTilesOverlay = TilesOverlay(mProvider, requireContext());

        mapView.getOverlays().add(mTilesOverlay);
        val location = (activity as MainActivity).mgr.getLastKnownLocation((activity as MainActivity).mgr.allProviders.get(0))
        if (location != null) {
            val icon = IconOverlay()
            icon.set(GeoPoint(location.latitude, location.longitude), resources.getDrawable(R.drawable.ic_baseline_location_on_24_red))

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
            polyline.isGeodesic = false
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

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


    companion object {
        fun newInstance(param1: String, param2: String) =
            RoadmapFragment().apply {
            }
    }
}
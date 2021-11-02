package com.qerlly.touristapp.application.main

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qerlly.touristapp.BuildConfig
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.MapPointsFragmentBinding
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView

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
        mapController.setZoom(12)
        val gPt = GeoPoint(51500000, -150000)
        mapController.setCenter(gPt)
        org.osmdroid.config.Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
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
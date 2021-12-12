package com.qerlly.touristapp.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.UserLocationsFragmentBinding
import com.qerlly.touristapp.model.user.UserLoc
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.IconOverlay

@AndroidEntryPoint
class UserLocationsFragment : Fragment() {
    private var _bind: UserLocationsFragmentBinding? = null
    private var mapController: MapController? = null

    companion object {
        fun newInstance() = UserLocationsFragment()
    }
    private val viewModel: UserLocationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = UserLocationsFragmentBinding.inflate(layoutInflater)
        prepareMap()
        viewModel.userLocations.onEach {
            if (it!= null){
                drawPointsOnTheMap(it)
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        return _bind?.root
    }

    private fun drawPointsOnTheMap(userLocs: List<UserLoc>){
        userLocs.forEach{
            val icon = IconOverlay()
            var drawable: Drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_black)
            if (it.ifGuide){
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_red)
            } else if (it.ifSelfLoc) {
                drawable = resources.getDrawable(R.drawable.ic_baseline_location_on_24_green)
            }
            icon.set(GeoPoint(it.lat, it.long), drawable)
            _bind?.mapView?.overlays?.add(icon)
            if (it.ifSelfLoc) {
                mapController!!.setCenter(GeoPoint(it.lat, it.long))
            }
        }
    }

    private fun prepareMap() {
        _bind!!.mapView.setBuiltInZoomControls(true)
        _bind!!.mapView.setMultiTouchControls(true)
        mapController = _bind!!.mapView.controller as MapController
        mapController!!.setZoom(19)
        /*mapController!!.setCenter(GeoPoint(54.333, 54.25))*/
    }

}
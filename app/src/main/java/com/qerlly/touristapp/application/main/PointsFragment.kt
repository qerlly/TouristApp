package com.qerlly.touristapp.application.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.application.main.adapters.FaqListAdapter
import com.qerlly.touristapp.application.main.viewmodels.PointsViewModel
import com.qerlly.touristapp.databinding.PointsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.recyclerview.widget.LinearLayoutManager
import com.qerlly.touristapp.R
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.IconOverlay

@AndroidEntryPoint
class PointsFragment : Fragment() {
    private var _bind: PointsFragmentBinding? = null
    private var mapController: MapController? = null
    private val pts: MutableList<GeoPoint> = ArrayList()

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private val viewModel: PointsViewModel by viewModels()

    companion object {
        fun newInstance() = PointsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        val adapter = FaqListAdapter(viewModel::onCardClicked)
        val manager = LinearLayoutManager(requireContext())
        _bind = PointsFragmentBinding.inflate(layoutInflater)
        _bind?.points?.setLayoutManager(manager)
        _bind?.points?.adapter = adapter
        viewModel.pointsNameDesc.onEach {
            if (it == null) {
                _bind?.pointsProgress?.visibility = View.VISIBLE
                _bind?.points?.visibility = View.GONE
            } else {
                _bind?.pointsProgress?.visibility = View.GONE
                _bind?.points?.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.pointsCoordinates.onEach { points ->
            points?.forEach { point ->
                pts.add(GeoPoint(point.lat, point.long))
            }
            drawPointsOnMap()
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        return _bind?.root
    }

    private fun drawPointsOnMap(){
        _bind!!.mapView.setBuiltInZoomControls(true)
        _bind!!.mapView.setMultiTouchControls(true)
        mapController = _bind!!.mapView.controller as MapController
        mapController!!.setZoom(19)
        pts.forEach { point ->
            run {
                val icon = IconOverlay()
                icon.set(GeoPoint(point.latitude, point.longitude), resources.getDrawable(R.drawable.ic_baseline_location_on_24))
                _bind?.mapView!!.overlays.add(icon)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }

}
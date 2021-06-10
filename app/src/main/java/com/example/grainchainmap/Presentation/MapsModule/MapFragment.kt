package com.example.grainchainmap.Presentation.MapsModule

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.grainchainmap.Presentation.services.TrackingService
import com.example.grainchainmap.R
import com.example.grainchainmap.databinding.FragmentMapBinding
import com.example.grainchainmap.domain.entities.RutaEntity
import com.example.grainchainmap.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.grainchainmap.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.grainchainmap.utils.Constants.ACTION_STOP_SERVICE
import com.example.grainchainmap.utils.Constants.MAP_ZOOM
import com.example.grainchainmap.utils.Constants.POLYLINE_WIDTH
import com.example.grainchainmap.utils.Helpers
import com.example.grainchainmap.utils.Permissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class MapFragment : Fragment(), MyRouteRecyclerViewAdapter.RouteItemListener, EasyPermissions.PermissionCallbacks  {

    private var _binding:FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var map:GoogleMap
    private lateinit var viewModel: MapViewModel
    private lateinit var myAdapter: MyRouteRecyclerViewAdapter
    private var columnCount = 1
    private var isTracking = false
    private var pathPoint = mutableListOf<LatLng>()
    private var curTimeInMillis = 0L
    private var totalDistance = 0f

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        val sydney = LatLng(20.35662107969063, -102.02478747217472)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16.5f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        myAdapter = MyRouteRecyclerViewAdapter(this)
        binding.stopStartBtn.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.green_500)

        val bottomSheetList = binding.bottomSheet.list
        if (bottomSheetList is RecyclerView) {
            with(bottomSheetList) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = myAdapter
            }
        }

        binding.stopStartBtn.setOnClickListener {
            toggleRun()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        if(Permissions.hasLocationPermission(requireContext())){
            startMapLocation()
            viewModel.loadRoutes()
        } else{
            Permissions.requestLocationPermission(this)
        }
    }

    private fun startMapLocation(){
        suscribeToObservers()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun suscribeToObservers(){
        viewModel.observableRoutes().observe(viewLifecycleOwner, Observer {
            myAdapter.reloadRoutes(it)
        })

        TrackingService.isTracking.observe(viewLifecycleOwner, {
            updateTracking(it)
        })

        TrackingService.pathPoint.observe(viewLifecycleOwner, {
            pathPoint = it
            addLatestPolyline()
            moveCamera()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, {
            curTimeInMillis = it
            val formattedTime = Helpers.getFoormatteStopWachTime(curTimeInMillis, true)
            binding.tvTimeCount.text = formattedTime
        })
    }

    private fun toggleRun(){
        if(isTracking){
            sendComandtoService(ACTION_STOP_SERVICE)
            saveProcess(pathPoint)
        }
        else{
            cleanScreenAndValues()
            sendComandtoService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(isTracking){
            binding.stopStartBtn.setImageResource(R.drawable.ic_stop)
            binding.stopStartBtn.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.red_500)
        }
        else{
            binding.stopStartBtn.setImageResource(R.drawable.ic_run)
            binding.stopStartBtn.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.green_500)
        }
    }

//    private fun zoomToSeeWholeTrack(){
//        val bounds = LatLngBounds.Builder()
//        for(pos in pathPoint){
//            bounds.include(pos)
//        }
//
//        map?.moveCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                bounds.build(),
//                binding.mainLayout.width,
//                binding.mainLayout.height,
//                (binding.mainLayout.height * 0.05f).toInt()
//            )
//        )
//    }

    private fun moveCamera(){
        if(pathPoint.isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoint.last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun cleanScreenAndValues(){
        totalDistance = 0f
        binding.tvTimeCount.text = getString(R.string.zeroTime)
        map?.clear()
    }

    private fun addLatestPolyline(){
        if(pathPoint.isNotEmpty() && pathPoint.size>1){
            val preLastLasLong = pathPoint[pathPoint.size - 2]
            val lstLasLong = pathPoint.last()

            val polylineOptions = PolylineOptions()
                .color(R.color.red_500)
                .width(POLYLINE_WIDTH)
                .add(preLastLasLong)
                .add(lstLasLong)
            map?.addPolyline(polylineOptions)

            val result = FloatArray(1)
            Location.distanceBetween(
                preLastLasLong.latitude,
                preLastLasLong.longitude,
                lstLasLong.latitude,
                lstLasLong.longitude,
                result
            )
            totalDistance += result[0]
        }

    }

    private fun saveProcess(locations:MutableList<LatLng>) {
        MaterialDialog(requireContext()).show {
            message(R.string.alert_add_route_messaje)
            input(allowEmpty = false) { dialog, text ->

                if(locations.isNotEmpty()) {
                    //zoomToSeeWholeTrack()
                    viewModel.addRoute(
                        RutaEntity(
                            name = text.toString(),
                            km = Helpers.convertMetersToKM(totalDistance),
                            time = Helpers.getFoormatteStopWachTime(curTimeInMillis),
                            latlongList = locations
                        )
                    )
                    Toast.makeText( requireContext(), getString(R.string.message_save_route), Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(requireContext(), getString(R.string.messahe_nolocations), Toast.LENGTH_LONG).show()
                }

            }
            negativeButton {
                //cleanScreen()
            }
            positiveButton(R.string.alerts_save)
            negativeButton(R.string.alerts_discard)
        }
    }

    private fun sendComandtoService(action:String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }

    override fun onclickRouteItem(v: View, route: RutaEntity) {
        //Toast.makeText(requireContext(), "${route.name} son ${route.km}", Toast.LENGTH_SHORT).show()
        val action = MapFragmentDirections.actionMapFragmentToDetailsFragment(route)
        findNavController().navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        } else{
            Permissions.requestLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), getString(R.string.permisos_ok), Toast.LENGTH_SHORT).show()
        startMapLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
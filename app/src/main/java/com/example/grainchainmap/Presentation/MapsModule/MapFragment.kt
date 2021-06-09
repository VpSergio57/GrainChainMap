package com.example.grainchainmap.Presentation.MapsModule

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.grainchainmap.GrainChainMapApplication
import com.example.grainchainmap.R
import com.example.grainchainmap.databinding.FragmentMapBinding
import com.example.grainchainmap.domain.entities.RutaEntity
import com.example.grainchainmap.utils.Permissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapFragment : Fragment(), MyRouteRecyclerViewAdapter.RouteItemListener, EasyPermissions.PermissionCallbacks  {

    private var _binding:FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAdapter: MyRouteRecyclerViewAdapter

    private var columnCount = 1

    private val callback = OnMapReadyCallback { googleMap ->

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

        //myAdapter.addRoutes( provData() )

        binding.stopStartBtn.setOnClickListener {
            saveProcess()//Deberia de ir en el MVVM
        }

        getRutesFromRoom()

        return binding.root
    }

    private fun getRutesFromRoom(){
        CoroutineScope(Dispatchers.IO).launch{
            val routes = GrainChainMapApplication.database.routeDao().getAllRoutes()
            myAdapter.addRoutes( routes )
        }
    }

    private fun saveProcess() {
        MaterialDialog(requireContext()).show {
            message(R.string.alert_add_route_messaje)
            input(allowEmpty = true) { dialog, text ->
                // Text submitted with the action button, might be an empty string`

//                val esta = RutaEntity(7, "Ruta Casa de Goyis", 2.7f, "00:30" , arrayListOf( LatLngData(20.340957872197734, -102.03038005241035), LatLngData(20.380957872197734, -102.00038005241035) ))
//
//                Thread {
//                    GrainChainMapApplication.database.routeDao().addRoute(esta)
//                }.start()

            }
            positiveButton(R.string.alerts_save)
            negativeButton(R.string.alerts_discard)
        }
    }


//    fun tempRouteGenerator():ArrayList<LatLngData>{
//        var list= arrayListOf<LatLngData>()
//        var tempLat = 20.356621
//        var temoLong = -102.024787
//        var coorInit = LatLngData(tempLat, temoLong)
//
//        list.add(coorInit)
//
//        for(i in 1..10){
//
//            tempLat += 0.005f
//            temoLong += 0.005f
//            list.add( LatLngData(tempLat, temoLong))
//        }
//        return list
//    }

//    fun provData() : ArrayList<RutaEntity>{
//        var myList = arrayListOf<RutaEntity>()
//
//        myList.add(RutaEntity(1, "Ruta Casa de Llamitas", 6.6f,"06:09" ,arrayListOf( LatLngData(20.361620999888242 ,-102.01978700011176), LatLngData(20.406620998882413 ,-101.97478700111759) ) ))
//        myList.add(RutaEntity(2, "Ruta Casa de Alma", 0.9f,"00:45" , arrayListOf( LatLngData(20.353564716826344, -102.03763838137967), LatLngData(20.393564716826344, -102.00763838137967) ) ))
//        myList.add(RutaEntity(7, "Ruta Casa de Goyis", 2.7f, "00:30" , arrayListOf( LatLngData(20.340957872197734, -102.03038005241035), LatLngData(20.380957872197734, -102.00038005241035) )))
//
//        return myList
//    }

    private fun startMapLocation(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(Permissions.hasLocationPermission(requireContext())){
            startMapLocation()
        }else{
            Permissions.requestLocationPermission(this)
        }
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
        Toast.makeText(requireContext(), "Permisos Aceptados!!", Toast.LENGTH_SHORT).show()
        startMapLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
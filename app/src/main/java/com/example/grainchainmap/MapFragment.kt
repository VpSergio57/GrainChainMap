package com.example.grainchainmap

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
import com.example.grainchainmap.databinding.FragmentMapBinding
import com.example.grainchainmap.placeholder.LatLngData
import com.example.grainchainmap.placeholder.RouteEntity
import com.example.grainchainmap.utils.Permissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

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

        myAdapter.addRoutes( provData() )

        binding.stopStartBtn.setOnClickListener {
            //Start and Stop services
        }

        return binding.root
    }

    fun provData() : ArrayList<RouteEntity>{
        var myList = arrayListOf<RouteEntity>()

        myList.add(RouteEntity(1, "Ruta del amor", 6.6f,"06:09" , arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(2, "Ruta del 69", 0.9f,"00:45" , arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(3, "Ruta Cerro Grande", 14.9f,"01:25" , arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(4, "Ruta Casa de Goyis", 18.3f,"02:35" , arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(5, "Ruta Casa Llamitas", 1.9f,"00:15" , arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(6, "Ruta Rio Grande", 9.8f, "00:10" ,arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(RouteEntity(7, "Ruta Casa de Alma", 2.7f, "00:30" ,arrayListOf( LatLngData(0.1, 1.1)) ))

        return myList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(Permissions.hasLocationPermission(requireContext())){
            startMapLocation()
        }else{
            Permissions.requestLocationPermission(this)
        }
    }

    private fun startMapLocation(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onclickRouteItem(v: View, route: RouteEntity) {
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
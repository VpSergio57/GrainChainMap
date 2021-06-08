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
import com.example.grainchainmap.placeholder.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), MyRouteRecyclerViewAdapter.RouteItemListener {

    private var _binding:FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAdapter: MyRouteRecyclerViewAdapter

    private var columnCount = 1

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
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
            val action = MapFragmentDirections.actionMapFragmentToDetailsFragment("HOLA SECOND FRAGMENT")
            findNavController().navigate(action)
        }

        return binding.root
    }

    fun provData() : ArrayList<Route>{
        var myList = arrayListOf<Route>()

        myList.add(Route(1, "Ruta del amor", 6.6f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(2, "Ruta del 69", 0.9f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(3, "Ruta Cerro Grande", 14.9f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(4, "Ruta Casa de Goyis", 18.3f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(5, "Ruta Casa Llamitas", 1.9f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(6, "Ruta Rio Grande", 9.8f, arrayListOf( LatLngData(0.1, 1.1)) ))
        myList.add(Route(7, "Ruta Casa de Alma", 2.7f, arrayListOf( LatLngData(0.1, 1.1)) ))

        return myList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onclickRouteItem(v: View, route: Route) {
        Toast.makeText(requireContext(), "${route.name} son ${route.km}", Toast.LENGTH_SHORT).show()
    }
}
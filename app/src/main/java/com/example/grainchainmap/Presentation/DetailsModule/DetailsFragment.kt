package com.example.grainchainmap.Presentation.DetailsModule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grainchainmap.R
import com.example.grainchainmap.databinding.FragmentDetailsBinding
import com.example.grainchainmap.domain.entities.RutaEntity
import com.example.grainchainmap.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dmax.dialog.SpotsDialog

class DetailsFragment : Fragment() {
//
//    val argsString:DetailsFragmentArgs by navArgs()
    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailsViewMovel
    private lateinit var dialog: AlertDialog
    private lateinit var map: GoogleMap
    private lateinit var route: RutaEntity
    private val args by navArgs<DetailsFragmentArgs>()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(20.35662107969063, -102.02478747217472)

        addPolyline(route.latlongList)

        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( route.latlongList[0],15.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        route = args.currentRoute

        dialog = SpotsDialog.Builder().setContext(context)
            .setCancelable(false)
            .build()

        (requireActivity() as AppCompatActivity).supportActionBar?.title = route.name
        // Inflate the layout for this fragment

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.tvDistancia.text = "${route.km} KM"
        binding.tvTiempo.text = route.time

        binding.tvDeleteRoute.setOnClickListener {
            viewModel.deleteRoute(route)
            dialog.show()

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewMovel::class.java)
        viewModel.observableOnDeleteRoute().observe(viewLifecycleOwner, {
            if(it){
                dialog.dismiss()
                findNavController().navigateUp()
            }
        })
    }

    private fun addPolyline(points: MutableList<LatLng>){
        if(points.isNotEmpty() && points.size>1){

            for(i in 1 until points.size){
                Log.d("DETAILS", "${points[i-1]} , ${points[i]}")
                val preLastLasLong = points[i-1]
                val lstLasLong = points[i]
                val polylineOptions = PolylineOptions()
                    .color(R.color.red_500)
                    .width(Constants.POLYLINE_WIDTH)
                    .add(preLastLasLong)
                    .add(lstLasLong)
                map?.addPolyline(polylineOptions)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
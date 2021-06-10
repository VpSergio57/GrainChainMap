package com.example.grainchainmap.Presentation.DetailsModule

import android.app.AlertDialog
import android.os.Bundle
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dmax.dialog.SpotsDialog

class DetailsFragment : Fragment() {
//
//    val argsString:DetailsFragmentArgs by navArgs()
    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailsViewMovel
    private lateinit var dialog: AlertDialog
    private val args by navArgs<DetailsFragmentArgs>()

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(20.35662107969063, -102.02478747217472)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16.5f))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val route = args.currentRoute

        dialog = SpotsDialog.Builder().setContext(context)
            .setCancelable(false)
            .build()

        (requireActivity() as AppCompatActivity).supportActionBar?.title = route.name
        // Inflate the layout for this fragment

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.tvDistancia.text = "${route.km} KM"
        binding.tvTiempo.text = "${route.time} HRS"

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
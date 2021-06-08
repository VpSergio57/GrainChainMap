package com.example.grainchainmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs

class DetailsFragment : Fragment() {

    val args:DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val nameRouteTest = args.routeName

        Toast.makeText(requireContext(), "$nameRouteTest", Toast.LENGTH_SHORT).show()

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

}
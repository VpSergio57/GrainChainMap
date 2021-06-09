package com.example.grainchainmap.Presentation.MapsModule

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.grainchainmap.databinding.FragmentItemRouteBinding
import com.example.grainchainmap.domain.entities.RutaEntity
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyRouteRecyclerViewAdapter( private val listener: RouteItemListener
) : RecyclerView.Adapter<MyRouteRecyclerViewAdapter.ViewHolder>() {

    interface RouteItemListener{
        fun onclickRouteItem(v:View, route: RutaEntity)
    }

    private val routes = mutableListOf<RutaEntity>()
    private lateinit var context: Context

    fun addRoutes( myRoutes: MutableList<RutaEntity>){
        this.routes.addAll(myRoutes)
        notifyDataSetChanged()
    }

    fun clearRoutes(){
        this.routes.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemRouteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(routes[position])
    override fun getItemCount(): Int = routes.size

    inner class ViewHolder(binding: FragmentItemRouteBinding, private val listener: RouteItemListener) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        private  lateinit var route: RutaEntity
        val tvKm: TextView = binding.tvKm
        val tvRouteName: TextView = binding.tvRouteName

        init{
            binding.root.setOnClickListener(this)
        }

        fun bind(route: RutaEntity){
            this.route = route
            tvKm.text = "${route.km} Km"
            tvRouteName.text = route.name
        }

        override fun onClick(v: View?) {
            listener.onclickRouteItem(v!!, this.route)
        }

    }

}
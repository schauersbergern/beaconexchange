package com.example.beaconexchange.ui.whitelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.beaconexchange.R
import com.example.localdatasource.entities.Device
import kotlinx.android.synthetic.main.fragment_whitelist_item.view.*

class WhiteListAdapter(private val myDataset: List<Device>) :
    RecyclerView.Adapter<WhiteListAdapter.WLViewHolder>() {

    class WLViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WLViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_whitelist_item, parent, false) as LinearLayout

        return WLViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: WLViewHolder, position: Int) {
        holder.linearLayout.device_name.text = "Device $position"
        holder.linearLayout.device_id.text = myDataset[position].uid
    }

    override fun getItemCount() = myDataset.size
}
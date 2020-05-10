package com.example.beaconexchange.ui.whitelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beaconexchange.R
import com.example.localdatasource.entities.Device

class WhiteListAdapter(private val myDataset: List<Device>) :
    RecyclerView.Adapter<WhiteListAdapter.WLViewHolder>() {

    class WLViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WLViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_whitelist_item, parent, false) as TextView

        return WLViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WLViewHolder, position: Int) {
        holder.textView.text = myDataset[position].uid
    }

    override fun getItemCount() = myDataset.size
}
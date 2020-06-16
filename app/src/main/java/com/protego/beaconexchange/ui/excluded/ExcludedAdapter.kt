package com.protego.beaconexchange.ui.excluded

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.protego.beaconexchange.R
import com.protego.localdatasource.entities.Device
import kotlinx.android.synthetic.main.fragment_excluded_item.view.*

class ExcludedAdapter(private val myDataset: List<Device>, private val onclick : (String) -> Unit) :
    RecyclerView.Adapter<ExcludedAdapter.WLViewHolder>() {

    class WLViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WLViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_excluded_item, parent, false) as LinearLayout

        return WLViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: WLViewHolder, position: Int) {
        val uid = myDataset[position].uid

        holder.linearLayout.device_name.text = "$position."
        holder.linearLayout.device_id.text = uid

        holder.linearLayout.delete_button.setOnClickListener {onclick(uid)}
    }

    override fun getItemCount() = myDataset.size
}
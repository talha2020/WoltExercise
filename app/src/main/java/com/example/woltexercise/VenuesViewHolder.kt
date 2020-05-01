package com.example.woltexercise

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.woltexercise.data.Results
import com.squareup.picasso.Picasso

class VenuesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    GenericAdapter.Binder<Results>  {

    private var venueIv = itemView.findViewById<ImageView>(R.id.venueIv)
    private var venueNameTv = itemView.findViewById<TextView>(R.id.venueNameTv)
    private var venueDescriptionTv = itemView.findViewById<TextView>(R.id.venueDescriptionTv)

    override fun bind(data: Results) {
        Picasso.get().load(data.listImage).into(venueIv)
        //TODO: put check for array elements here
        venueNameTv.text = data.name[0].value
        venueDescriptionTv.text = data.shortDescription[0].value
    }

}
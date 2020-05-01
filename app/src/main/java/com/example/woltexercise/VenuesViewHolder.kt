package com.example.woltexercise

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.woltexercise.data.Place
import com.squareup.picasso.Picasso

class VenuesViewHolder(itemView: View,
                       private val onFavoriteClicked: (Int) -> Unit): RecyclerView.ViewHolder(itemView),
    GenericAdapter.Binder<Place>  {

    private var venueIv = itemView.findViewById<ImageView>(R.id.venueIv)
    private var favoriteIv = itemView.findViewById<ImageView>(R.id.favoriteIv)
    private var venueNameTv = itemView.findViewById<TextView>(R.id.venueNameTv)
    private var venueDescriptionTv = itemView.findViewById<TextView>(R.id.venueDescriptionTv)

    override fun bind(data: Place) {
        Picasso.get().load(data.listImage).into(venueIv)
        //TODO: put check for array elements here
        venueNameTv.text = data.name[0].value
        venueDescriptionTv.text = data.shortDescription[0].value

        favoriteIv.setOnClickListener { onFavoriteClicked(adapterPosition) }

        if (data.favourite)
            favoriteIv.setImageResource(R.drawable.ic_favorite)
        else
            favoriteIv.setImageResource(R.drawable.ic_favorite_border)
    }

}
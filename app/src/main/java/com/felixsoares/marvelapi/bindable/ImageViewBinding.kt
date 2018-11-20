package com.felixsoares.marvelapi.bindable

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.felixsoares.marvelapi.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageurl")
fun ImageView.setImageurl(url: String?) {
    Picasso.get()
        .load(url)
        .error(R.drawable.marvel)
        .placeholder(R.drawable.marvel)
        .into(this)
}
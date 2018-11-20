package com.felixsoares.marvelapi.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.felixsoares.marvelapi.R
import com.felixsoares.marvelapi.data.database.RMComic
import com.felixsoares.marvelapi.data.utilities.UserInteraction
import kotlinx.android.synthetic.main.comic_recycler_item.view.*

class ComicAdapter(
    private val context: Context,
    private val userInteraction: UserInteraction? = null
) : RecyclerView.Adapter<ComicViewHolder>() {

    val comics = mutableListOf<RMComic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comic_recycler_item, parent, false)
        return ComicViewHolder(view, userInteraction)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bindView(comics[position])
    }

    override fun getItemCount() = comics.size

    fun addComics(comics: List<RMComic>) {
        this.comics.addAll(comics)
        notifyDataSetChanged()
    }
}

class ComicViewHolder(
    itemView: View,
    private val userInteraction: UserInteraction?
) : RecyclerView.ViewHolder(itemView) {

    fun bindView(comic: RMComic) {
        itemView.txtTitle.text = comic.title

        if (comic.description != null) {
            itemView.txtDescription.visibility = View.VISIBLE
            itemView.txtDescription.text = comic.description
        } else {
            itemView.txtDescription.visibility = View.GONE
        }

        setListner(comic.id!!)
    }

    private fun setListner(idCharacter: String) {
        if (userInteraction != null) {
            itemView.setOnClickListener {
                userInteraction.OnClick(idCharacter, itemView.txtTitle)
            }
        }
    }

}
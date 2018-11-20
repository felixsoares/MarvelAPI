package com.felixsoares.marvelapi.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.felixsoares.marvelapi.R
import com.felixsoares.marvelapi.bindable.setImageurl
import com.felixsoares.marvelapi.data.database.RMCharacter
import com.felixsoares.marvelapi.data.utilities.UserInteraction
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_recycler_item.view.*

class CharacterAdapter(
    private val context: Context,
    private val userInteraction: UserInteraction? = null
) : RecyclerView.Adapter<CharacterViewHolder>() {

    val characters = mutableListOf<RMCharacter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_recycler_item, parent, false)
        return CharacterViewHolder(view, userInteraction)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(characters[position])
    }

    override fun getItemCount() = characters.size

    fun addCharacters(chars: List<RMCharacter>) {
        characters.addAll(chars)
        notifyDataSetChanged()
    }
}

class CharacterViewHolder(
    itemView: View,
    private val userInteraction: UserInteraction?
) : RecyclerView.ViewHolder(itemView) {

    fun bindView(character: RMCharacter) {
        itemView.imgChar.setImageurl(character.thumbnail)
        itemView.txtName.text = character.name
        itemView.txtDescription.text = character.description.takeIf { it != "" } ?: "No description"

        setListner(character.id!!)
    }

    private fun setListner(idCharacter: String) {
        if (userInteraction != null) {
            itemView.setOnClickListener {
                userInteraction.OnClick(idCharacter, itemView.imgChar)
            }
        }
    }

}
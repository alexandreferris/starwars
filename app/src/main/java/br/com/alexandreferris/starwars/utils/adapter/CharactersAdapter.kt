package br.com.alexandreferris.starwars.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alexandreferris.starwars.R
import br.com.alexandreferris.starwars.model.CharacterSearch
import br.com.alexandreferris.starwars.utils.constants.Credentials
import br.com.alexandreferris.starwars.utils.getUrlId
import kotlinx.android.synthetic.main.item_character_search.view.*
import org.apache.commons.lang3.StringUtils

class CharactersAdapter(private val context: Context, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    private lateinit var characters: List<CharacterSearch>

    fun setCharactersList(characters: List<CharacterSearch>) {
        this.characters = characters
    }

    // Listener adapter for setOnClickListener
    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int) -> Unit): T {
        itemView.setOnClickListener {
            val characterId: Int = characters[adapterPosition].url.getUrlId("people")

            event.invoke(characterId)
        }
        return this
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, character: CharacterSearch) {
            itemView.textViewCharacterName.text = character.name
            itemView.textViewCharacterBirthYear.text = context.resources.getString(R.string.character_birth_year, character.birthYear)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character_search, parent, false)
        return ViewHolder(view).listen { pos ->
            itemClickListener.invoke(pos)
        }
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, characters[position])
    }
}
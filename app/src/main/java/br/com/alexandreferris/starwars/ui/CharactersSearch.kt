package br.com.alexandreferris.starwars.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.alexandreferris.starwars.App
import br.com.alexandreferris.starwars.R
import br.com.alexandreferris.starwars.model.CharacterSearch
import br.com.alexandreferris.starwars.utils.adapter.CharactersAdapter
import br.com.alexandreferris.starwars.utils.adapter.EndlessScrollListener
import br.com.alexandreferris.starwars.utils.observeLoadingBar
import br.com.alexandreferris.starwars.viewmodel.CharactersSearchVM
import kotlinx.android.synthetic.main.activity_characters_search.*
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import javax.inject.Inject
import android.support.v4.view.ViewCompat
import android.view.View.OnUnhandledKeyEventListener
import android.support.annotation.RequiresApi
import android.view.KeyEvent


class CharactersSearch : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var charactersSearchVM: CharactersSearchVM

    private lateinit var charactersAdapter: CharactersAdapter
    private lateinit var scrollListener: EndlessScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_search)

        (application as App).appComponent.inject(this)

        configureUIButtons()
        configureObservableViewModel()
        charactersSearchVM.fetchCharacters()
    }

    private fun configureUIButtons() {
        buttonLoadMore.setOnClickListener(this)
        buttonTryAgain.setOnClickListener(this)
    }

    private fun configureObservableViewModel() {
        charactersSearchVM.getLoading().observe(this, observeLoadingBar(loadingBar))

        charactersSearchVM.getError().observe(this, Observer { show ->
            hideShowLoadMoreAndNoSearchResult(show!!)
        })

        charactersSearchVM.getCharactersSearch().observe(this,  Observer { characterList ->
            hideShowLoadMoreAndNoSearchResult(false)
            hideShowRecyclerViewNoResult(characterList!!)
        })
    }

    private fun hideShowLoadMoreAndNoSearchResult(show: Boolean) {
        relativeLayoutLoadMoreError.visibility = View.GONE
        constraintLayoutNoResult.visibility = View.GONE

        if (show) {
            val isStartingPage = (charactersSearchVM.currentPage == charactersSearchVM.startingPage)

            if (!isStartingPage)
                Snackbar.make(constraintLayoutCharactersSearch, R.string.error_loading_characters, Snackbar.LENGTH_LONG).show()
            relativeLayoutLoadMoreError.visibility = if (!isStartingPage) View.VISIBLE else View.GONE
            constraintLayoutNoResult.visibility = if (isStartingPage) View.VISIBLE else View.GONE
        }
    }

    private fun hideShowRecyclerViewNoResult(list: List<CharacterSearch>) {
        val isListEmptyAndSearchNotEmpty = (list.size == NumberUtils.INTEGER_ZERO && !StringUtils.isEmpty(charactersSearchVM.searchQuery))

        recyclerView.visibility = if (!isListEmptyAndSearchNotEmpty) View.VISIBLE else View.GONE
        textViewSearchNoResult.visibility = if (isListEmptyAndSearchNotEmpty) View.VISIBLE else View.GONE

        if (!isListEmptyAndSearchNotEmpty)
            displayCharacters(list)
    }

    private fun setupRecyclerView(characters: List<CharacterSearch>, visibleThreshold: Int) {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        charactersAdapter = CharactersAdapter(this) { characterId ->
            val characterSelectedDetails = Intent(this, CharacterDetails::class.java)
            characterSelectedDetails.putExtra("CHARACTER_ID", characterId)

            startActivity(characterSelectedDetails)
        }
        charactersAdapter.setCharactersList(characters)
        recyclerView.adapter = charactersAdapter

        if (characters.size >= charactersSearchVM.limit) {
            scrollListener = object : EndlessScrollListener(linearLayoutManager, visibleThreshold) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    charactersSearchVM.currentPage = page
                    charactersSearchVM.fetchCharacters()
                }
            }

            recyclerView.addOnScrollListener(scrollListener)
        }
    }

    private fun displayCharacters(listCharacters: List<CharacterSearch>) {
        recyclerView.visibility = View.VISIBLE
        if (charactersSearchVM.currentPage == charactersSearchVM.startingPage) {
            setupRecyclerView(listCharacters, (charactersSearchVM.visibleThreshold - NumberUtils.INTEGER_ONE))
        } else {
            charactersAdapter.setCharactersList(listCharacters)
            charactersAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)

        val searchView: SearchView? = searchItem.actionView as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                scrollListener.resetState()
                charactersSearchVM.reset()
                charactersSearchVM.searchQuery = query
                charactersSearchVM.fetchCharacters()
                return false
            }

        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                scrollListener.resetState()
                resetCharacterList()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun resetCharacterList() {
        charactersSearchVM.reset()
        charactersSearchVM.fetchCharacters()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonTryAgain, R.id.buttonLoadMore -> {
                relativeLayoutLoadMoreError.visibility = View.GONE
                charactersSearchVM.fetchCharacters()
            }
        }
    }
}

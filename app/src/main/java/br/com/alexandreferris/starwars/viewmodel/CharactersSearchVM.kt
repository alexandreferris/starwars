package br.com.alexandreferris.starwars.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.CharacterSearch
import br.com.alexandreferris.starwars.model.CharactersSearchResponse
import br.com.alexandreferris.starwars.utils.constants.Credentials
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

open class CharactersSearchVM @Inject constructor(private val networkApi: StarWarsApi): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val listCharacters: ArrayList<CharacterSearch> = ArrayList()
    private val characters: MutableLiveData<List<CharacterSearch>> = MutableLiveData()
    private val charactersLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    val limit: Int = Credentials.MAX_CHARACTERS_PER_PAGE_SEARCH
    val startingPage: Int = Credentials.SEARCH_STARTING_PAGE
    var currentPage: Int = Credentials.SEARCH_STARTING_PAGE
    var visibleThreshold: Int = Credentials.SEARCH_STARTING_VISIBLE_THRESHOLD

    var searchQuery: String = StringUtils.EMPTY

    fun getCharactersSearch(): MutableLiveData<List<CharacterSearch>> {
        return characters
    }

    fun getError(): LiveData<Boolean> {
        return charactersLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchCharacters() {
        loading.value = true
        disposable.add(networkApi.getCharactersSearch(searchQuery, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<CharactersSearchResponse>() {
                override fun onError(e: Throwable) {
                    charactersLoadError.value = true
                    loading.value = false
                }

                override fun onSuccess(charactersSearchResponse: CharactersSearchResponse) {
                    charactersLoadError.value = false
                    loading.value = false

                    listCharacters.addAll(charactersSearchResponse.results)
                    visibleThreshold = listCharacters.size / currentPage
                    characters.value = listCharacters
                }
            })
        )
    }

    fun reset() {
        loading.value = false
        charactersLoadError.value = false
        listCharacters.clear()
        searchQuery = StringUtils.EMPTY
        currentPage = Credentials.SEARCH_STARTING_PAGE
        characters.value = listCharacters
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
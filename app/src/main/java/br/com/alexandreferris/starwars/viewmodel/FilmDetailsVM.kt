package br.com.alexandreferris.starwars.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.FilmResponse
import br.com.alexandreferris.starwars.utils.getUrlId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.math.NumberUtils
import javax.inject.Inject

open class FilmDetailsVM @Inject constructor(private val networkApi: StarWarsApi): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val filmsList: ArrayList<FilmResponse> = ArrayList()
    private val films: MutableLiveData<List<FilmResponse>> = MutableLiveData()
    private val filmsLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    fun getFilms(): MutableLiveData<List<FilmResponse>> {
        return films
    }

    fun getError(): LiveData<Boolean> {
        return filmsLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchFilms(filmsURL: List<String>) {
        if (filmsURL.size > NumberUtils.INTEGER_ZERO) {
            var a: Int = 0
            for (filmURL: String in filmsURL) {
                val filmId = filmURL.getUrlId("films")

                loading.value = true
                disposable.add(
                    networkApi.getFilmDetails(filmId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<FilmResponse>() {
                        override fun onError(e: Throwable) {
                            filmsLoadError.value = true
                            loading.value = false
                        }

                        override fun onSuccess(filmResponse: FilmResponse) {
                            filmsLoadError.value = false
                            loading.value = false

                            filmsList.add(filmResponse)
                            if (filmsURL.size == filmsList.size)
                                films.value = filmsList
                        }
                    })
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
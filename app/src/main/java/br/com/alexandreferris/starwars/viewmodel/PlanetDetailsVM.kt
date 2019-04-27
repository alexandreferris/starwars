package br.com.alexandreferris.starwars.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.PlanetResponse
import br.com.alexandreferris.starwars.utils.getUrlId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PlanetDetailsVM @Inject constructor(private val networkApi: StarWarsApi): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val planet: MutableLiveData<PlanetResponse> = MutableLiveData()
    private val planetLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    fun getPlanet(): MutableLiveData<PlanetResponse> {
        return planet
    }

    fun getError(): LiveData<Boolean> {
        return planetLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchPlanet(planetURL: String) {
        val planetId = planetURL.getUrlId("planets")

        loading.value = true
        disposable.add(
            networkApi.getPlanetDetails(planetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlanetResponse>() {
                    override fun onError(e: Throwable) {
                        planetLoadError.value = true
                        loading.value = false
                    }

                    override fun onSuccess(planetResponse: PlanetResponse) {
                        planetLoadError.value = false
                        loading.value = false

                        planet.value = planetResponse
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
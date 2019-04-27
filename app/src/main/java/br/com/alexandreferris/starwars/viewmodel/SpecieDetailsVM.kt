package br.com.alexandreferris.starwars.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.SpecieResponse
import br.com.alexandreferris.starwars.utils.getUrlId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.math.NumberUtils
import javax.inject.Inject

open class SpecieDetailsVM @Inject constructor(private val networkApi: StarWarsApi): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val specie: MutableLiveData<SpecieResponse> = MutableLiveData()
    private val specieLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    fun getSpecie(): MutableLiveData<SpecieResponse> {
        return specie
    }

    fun getError(): LiveData<Boolean> {
        return specieLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchSpecie(listSpecies: List<String>) {
        if (listSpecies.size > NumberUtils.INTEGER_ZERO) {
            for (specieURL: String in listSpecies) {
                val specieId = specieURL.getUrlId("species")

                loading.value = true
                disposable.add(
                    networkApi.getSpecieDetails(specieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<SpecieResponse>() {
                            override fun onError(e: Throwable) {
                                specieLoadError.value = true
                                loading.value = false
                            }

                            override fun onSuccess(specieResponse: SpecieResponse) {
                                specieLoadError.value = false
                                loading.value = false

                                specie.value = specieResponse
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
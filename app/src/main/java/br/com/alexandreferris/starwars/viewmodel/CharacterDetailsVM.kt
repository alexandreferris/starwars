package br.com.alexandreferris.starwars.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.CharacterData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class CharacterDetailsVM @Inject constructor(private val networkApi: StarWarsApi): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val character: MutableLiveData<CharacterData> = MutableLiveData()
    private val characterLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    fun getCharacter(): MutableLiveData<CharacterData> {
        return character
    }

    fun getError(): LiveData<Boolean> {
        return characterLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchCharacter(characterId: Int) {
        loading.value = true
        disposable.add(networkApi.getCharacterDetails(characterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<CharacterData>() {
                override fun onError(e: Throwable) {
                    characterLoadError.value = true
                    loading.value = false
                }

                override fun onSuccess(characterData: CharacterData) {
                    characterLoadError.value = false
                    loading.value = false

                    character.value = characterData
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
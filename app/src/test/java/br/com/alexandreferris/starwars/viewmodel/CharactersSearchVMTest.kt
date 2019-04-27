package br.com.alexandreferris.starwars.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexandreferris.starwars.BaseTest
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.CharacterSearch
import br.com.alexandreferris.starwars.model.CharactersSearchResponse
import com.squareup.moshi.Json
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CharactersSearchVMTest: BaseTest() {

    val mockedCharacterSearch = CharacterSearch(
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        StringUtils.EMPTY
    )

    val mockedCharacterSearchResponse = CharactersSearchResponse(
        NumberUtils.INTEGER_ZERO,
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        listOf(mockedCharacterSearch)
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: StarWarsApi

    private lateinit var charactersSearchVM: CharactersSearchVM

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(networkApi.getCharactersSearch(Matchers.anyString(), Matchers.anyInt()))
            .thenReturn(Single.just(mockedCharacterSearchResponse))

        charactersSearchVM = CharactersSearchVM(networkApi)
        charactersSearchVM.fetchCharacters()
    }

    @Test
    fun getSingleCharacterSearchWithNoError() {
        val characterSearchResult = charactersSearchVM.getCharactersSearch().value
        assertEquals(NumberUtils.INTEGER_ONE, characterSearchResult?.size)
        assertEquals(false, charactersSearchVM.getError().value)
        assertEquals(false, charactersSearchVM.getLoading().value)
    }

    @Test
    fun resetViewModelStates() {
        // Asserting Characters List is NOT Null
        assertNotNull(charactersSearchVM.getCharactersSearch().value)

        // Asserting searchQuery from ViewModel is NOT Empty
        charactersSearchVM.searchQuery = "Random Search String"
        assertFalse(charactersSearchVM.searchQuery.isEmpty())

        // Resets all necessary variables in ViewModel
        charactersSearchVM.reset()

        assertEquals(NumberUtils.INTEGER_ZERO, charactersSearchVM.getCharactersSearch().value?.size)
        assertEquals(StringUtils.EMPTY, charactersSearchVM.searchQuery)
    }
}
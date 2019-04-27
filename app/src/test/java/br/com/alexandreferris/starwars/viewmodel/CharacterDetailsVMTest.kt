package br.com.alexandreferris.starwars.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexandreferris.starwars.BaseTest
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.CharacterData
import com.squareup.moshi.Json
import io.reactivex.Single
import org.apache.commons.lang3.StringUtils
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CharacterDetailsVMTest: BaseTest() {

    val mockedCharacterData = CharacterData(
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        listOf(StringUtils.EMPTY),
        listOf(StringUtils.EMPTY)
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: StarWarsApi

    private lateinit var characterDetailsVM: CharacterDetailsVM

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(networkApi.getCharacterDetails(Matchers.anyInt()))
            .thenReturn(Single.just(mockedCharacterData))

        characterDetailsVM = CharacterDetailsVM(networkApi)
        characterDetailsVM.fetchCharacter(Matchers.anyInt())
    }

    @Test
    fun getSingleCharacterWithNoError() {
        val characterData = characterDetailsVM.getCharacter().value
        assertNotNull(characterData)
        assertEquals(false, characterDetailsVM.getError().value)
        assertEquals(false, characterDetailsVM.getLoading().value)
    }
}
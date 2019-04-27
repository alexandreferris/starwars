package br.com.alexandreferris.starwars.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexandreferris.starwars.BaseTest
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.FilmResponse
import io.reactivex.Single
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

class FilmDetailsVMTest: BaseTest() {

    val mockedFilmResponse = FilmResponse(
        StringUtils.EMPTY,
        StringUtils.EMPTY,
        StringUtils.EMPTY
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: StarWarsApi

    private lateinit var filmDetailsVM: FilmDetailsVM

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(networkApi.getFilmDetails(Matchers.anyInt()))
            .thenReturn(Single.just(mockedFilmResponse))

        filmDetailsVM = FilmDetailsVM(networkApi)
        filmDetailsVM.fetchFilms(listOf("films/1/", "films/2/"))
    }

    @Test
    fun getFilmsWithNoError() {
        val films = filmDetailsVM.getFilms().value
        assertNotNull(films)
        assertEquals(NumberUtils.INTEGER_TWO, films?.size)
        assertEquals(StringUtils.EMPTY, films?.get(NumberUtils.INTEGER_ZERO)?.title)
        assertEquals(StringUtils.EMPTY, films?.get(NumberUtils.INTEGER_ZERO)?.releaseDate)
        assertEquals(StringUtils.EMPTY, films?.get(NumberUtils.INTEGER_ZERO)?.openingCrawl)
        assertEquals(false, filmDetailsVM.getError().value)
        assertEquals(false, filmDetailsVM.getLoading().value)
    }
}
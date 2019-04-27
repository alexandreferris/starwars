package br.com.alexandreferris.starwars.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexandreferris.starwars.BaseTest
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.PlanetResponse
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

class PlanetDetailsVMTest: BaseTest() {

    val mockedPlanetResponse = PlanetResponse(
        StringUtils.EMPTY,
        StringUtils.EMPTY
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: StarWarsApi

    private lateinit var planetDetailsVM: PlanetDetailsVM

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(networkApi.getPlanetDetails(Matchers.anyInt()))
            .thenReturn(Single.just(mockedPlanetResponse))

        planetDetailsVM = PlanetDetailsVM(networkApi)
        planetDetailsVM.fetchPlanet("planets/1/")
    }

    @Test
    fun getSinglePlanetWithNoError() {
        val planet = planetDetailsVM.getPlanet().value
        assertNotNull(planet)
        assertEquals(StringUtils.EMPTY, planet?.name)
        assertEquals(StringUtils.EMPTY, planet?.population)
        assertEquals(false, planetDetailsVM.getError().value)
        assertEquals(false, planetDetailsVM.getLoading().value)
    }
}
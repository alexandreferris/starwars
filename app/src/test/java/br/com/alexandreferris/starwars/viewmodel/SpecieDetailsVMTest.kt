package br.com.alexandreferris.starwars.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexandreferris.starwars.BaseTest
import br.com.alexandreferris.starwars.data.remote.StarWarsApi
import br.com.alexandreferris.starwars.model.SpecieResponse
import io.reactivex.Single
import org.apache.commons.lang3.StringUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SpecieDetailsVMTest: BaseTest() {

    val mockedSpecieResponse = SpecieResponse(
        StringUtils.EMPTY,
        StringUtils.EMPTY
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApi: StarWarsApi

    private lateinit var specieDetailsVM: SpecieDetailsVM

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(networkApi.getSpecieDetails(Matchers.anyInt()))
            .thenReturn(Single.just(mockedSpecieResponse))

        specieDetailsVM = SpecieDetailsVM(networkApi)
        specieDetailsVM.fetchSpecie(listOf("species/1/", "species/2/"))
    }

    @Test
    fun getSingleSpecieWithNoError() {
        val specie = specieDetailsVM.getSpecie().value
        assertNotNull(specie)
        assertEquals(StringUtils.EMPTY, specie?.name)
        assertEquals(StringUtils.EMPTY, specie?.language)
        assertEquals(false, specieDetailsVM.getError().value)
        assertEquals(false, specieDetailsVM.getLoading().value)
    }
}
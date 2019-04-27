package br.com.alexandreferris.starwars.data.remote

import br.com.alexandreferris.starwars.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarWarsApi {

    @GET("people")
    fun getCharactersSearch(
        @Query("search") search: String?,
        @Query("page") page: Int?
    ): Single<CharactersSearchResponse>

    @GET("people/{characterId}")
    fun getCharacterDetails(
        @Path("characterId") characterId: Int
    ): Single<CharacterData>

    @GET("species/{specieId}")
    fun getSpecieDetails(
        @Path("specieId") specieId: Int
    ): Single<SpecieResponse>

    @GET("planets/{planetId}")
    fun getPlanetDetails(
        @Path("planetId") planetId: Int
    ): Single<PlanetResponse>

    @GET("films/{filmId}")
    fun getFilmDetails(
        @Path("filmId") filmId: Int
    ): Single<FilmResponse>
}
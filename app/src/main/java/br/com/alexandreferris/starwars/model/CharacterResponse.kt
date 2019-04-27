package br.com.alexandreferris.starwars.model

import com.squareup.moshi.Json


data class CharactersSearchResponse (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<CharacterSearch>
)

data class CharacterSearch (
    val name: String,
    @Json(name = "birth_year") val birthYear: String,
    val url: String
)

data class CharacterData (
    val name: String,
    @Json(name = "birth_year") val birthYear: String,
    val height: String,
    val homeworld: String,
    val films: List<String>,
    val species: List<String>
)


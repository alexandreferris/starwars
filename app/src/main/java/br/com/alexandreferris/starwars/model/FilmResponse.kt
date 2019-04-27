package br.com.alexandreferris.starwars.model

import com.squareup.moshi.Json

data class FilmResponse (
    val title: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "opening_crawl") val openingCrawl: String
)
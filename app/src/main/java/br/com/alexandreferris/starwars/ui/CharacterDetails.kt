package br.com.alexandreferris.starwars.ui

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.alexandreferris.starwars.App
import br.com.alexandreferris.starwars.R
import br.com.alexandreferris.starwars.model.CharacterData
import br.com.alexandreferris.starwars.model.FilmResponse
import br.com.alexandreferris.starwars.model.PlanetResponse
import br.com.alexandreferris.starwars.model.SpecieResponse
import br.com.alexandreferris.starwars.utils.adapter.MoviesPagerAdapter
import br.com.alexandreferris.starwars.utils.observeLoadingBar
import br.com.alexandreferris.starwars.utils.observeLoadingError
import br.com.alexandreferris.starwars.viewmodel.CharacterDetailsVM
import br.com.alexandreferris.starwars.viewmodel.FilmDetailsVM
import br.com.alexandreferris.starwars.viewmodel.PlanetDetailsVM
import br.com.alexandreferris.starwars.viewmodel.SpecieDetailsVM
import kotlinx.android.synthetic.main.activity_character_details.*
import org.apache.commons.lang3.math.NumberUtils
import javax.inject.Inject

class CharacterDetails : AppCompatActivity() {

    @Inject
    lateinit var characterDetailsVM: CharacterDetailsVM

    @Inject
    lateinit var specieDetailsVM: SpecieDetailsVM

    @Inject
    lateinit var planetDetailsVM: PlanetDetailsVM

    @Inject
    lateinit var filmDetailsVM: FilmDetailsVM

    private var characterId: Int = NumberUtils.INTEGER_ZERO

    private lateinit var pagerAdapter: MoviesPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        (application as App).appComponent.inject(this)

        configureUI()
        configureCharacterData()
    }

    private fun configureUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pagerAdapter = MoviesPagerAdapter(supportFragmentManager)
        viewPagerCharacterFilms.adapter = pagerAdapter
    }

    private fun configureCharacterData() {
        characterId = intent.getIntExtra("CHARACTER_ID", NumberUtils.INTEGER_ZERO)

        configureCharacterObservers()
        characterDetailsVM.fetchCharacter(characterId)
    }

    private fun configureCharacterObservers() {
        characterDetailsVM.getLoading().observe(this, observeLoadingBar(loadingBar))
        characterDetailsVM.getError().observe(this, observeLoadingError(textViewCharacterDetailsNoResult))
        characterDetailsVM.getCharacter().observe(this, Observer { characterData ->
            displayCharacterDetails(characterData!!)

            // Get Specie Data
            configureSpeciesObservers()
            specieDetailsVM.fetchSpecie(characterData.species)

            // Get Planet Data
            configurePlanetObservers()
            planetDetailsVM.fetchPlanet(characterData.homeworld)

            // Get Films Data
            configureFilmsObservers()
            filmDetailsVM.fetchFilms(characterData.films)
        })
    }

    private fun configureFilmsObservers() {
        filmDetailsVM.getLoading().observe(this, observeLoadingBar(loadingBar))
        filmDetailsVM.getError().observe(this, observeLoadingError(textViewCharacterDetailsNoResult))
        filmDetailsVM.getFilms().observe(this, Observer { filmsResponse ->
            displayFilmsDetails(filmsResponse!!)
        })
    }

    private fun configureSpeciesObservers() {
        specieDetailsVM.getLoading().observe(this, observeLoadingBar(loadingBar))
        specieDetailsVM.getError().observe(this, observeLoadingError(textViewCharacterDetailsNoResult))
        specieDetailsVM.getSpecie().observe(this, Observer { specieResponse ->
            displaySpecieDetails(specieResponse!!)
        })
    }

    private fun configurePlanetObservers() {
        planetDetailsVM.getLoading().observe(this, observeLoadingBar(loadingBar))
        planetDetailsVM.getError().observe(this, observeLoadingError(textViewCharacterDetailsNoResult))
        planetDetailsVM.getPlanet().observe(this, Observer { planetResponse ->
            displayPlanetDetails(planetResponse!!)
        })
    }

    private fun displayFilmsDetails(filmsResponse: List<FilmResponse>) {
        pagerAdapter.movies = filmsResponse
        pagerAdapter.notifyDataSetChanged()
        viewPagerCharacterFilms.offscreenPageLimit = filmsResponse.size
    }

    private fun displayPlanetDetails(planetResponse: PlanetResponse) {
        textViewSpecieHomeworld.text = resources.getString(R.string.specie_homeworld, planetResponse.name)
        textViewSpeciePopulation.text = resources.getString(R.string.specie_population, planetResponse.population)
    }

    private fun displaySpecieDetails(specieResponse: SpecieResponse) {
        textViewSpecieName.text = resources.getString(R.string.specie_name, specieResponse.name)
        textViewSpecieLanguage.text = resources.getString(R.string.specie_language, specieResponse.language)
    }

    private fun displayCharacterDetails(characterData: CharacterData) {
        supportActionBar?.title = resources.getString(R.string.title_character_details)

        textViewCharacterName.text = characterData.name
        textViewCharacterBirthYear.text = resources.getString(R.string.character_birth_year, characterData.birthYear)
        textViewCharacterHeight.text = resources.getString(R.string.character_height, characterData.height)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

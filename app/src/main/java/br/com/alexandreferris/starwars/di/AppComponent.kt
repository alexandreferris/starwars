package br.com.alexandreferris.starwars.di

import br.com.alexandreferris.starwars.ui.CharacterDetails
import br.com.alexandreferris.starwars.ui.CharactersSearch
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(target: CharactersSearch)
    fun inject(target: CharacterDetails)
}
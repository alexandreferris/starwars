package br.com.alexandreferris.starwars

import android.app.Application
import br.com.alexandreferris.starwars.di.AppComponent
import br.com.alexandreferris.starwars.di.AppModule
import br.com.alexandreferris.starwars.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
            DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
}
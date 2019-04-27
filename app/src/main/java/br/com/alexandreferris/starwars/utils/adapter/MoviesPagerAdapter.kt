package br.com.alexandreferris.starwars.utils.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.alexandreferris.starwars.model.FilmResponse
import br.com.alexandreferris.starwars.ui.MovieFragment
import android.os.Parcelable

class MoviesPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var movies: List<FilmResponse> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return MovieFragment.newInstance(movies[position])
    }

    override fun getCount(): Int {
        return movies.size
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}
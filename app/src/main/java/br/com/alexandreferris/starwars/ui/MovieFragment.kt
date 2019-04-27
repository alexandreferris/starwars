package br.com.alexandreferris.starwars.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.alexandreferris.starwars.R
import br.com.alexandreferris.starwars.model.FilmResponse
import br.com.alexandreferris.starwars.utils.formatUSDate
import br.com.alexandreferris.starwars.utils.helper.MovieHelper
import br.com.alexandreferris.starwars.utils.removeSingleLines

class MovieFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

        // Creates the view controlled by the fragment
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        val textViewMovieTitle = view.findViewById<TextView>(R.id.textViewMovieTitle)
        val textViewMovieReleaseDate = view.findViewById<TextView>(R.id.textViewMovieReleaseDate)
        val textViewMovieOpeningCrawl = view.findViewById<TextView>(R.id.textViewMovieOpeningCrawl)

        // Retrieve and display the movie data from the Bundle
        val args = arguments
        textViewMovieTitle.text = args?.getString(MovieHelper.KEY_TITLE)
        textViewMovieReleaseDate.text = resources.getString(R.string.film_release_date, args?.getString(MovieHelper.KEY_RELEASE_DATE)?.formatUSDate())
        textViewMovieOpeningCrawl.text = args?.getString(MovieHelper.KEY_OPENING_CRAWL)?.removeSingleLines()

        return view
    }

    companion object {

        // Method for creating new instances of the fragment
        fun newInstance(movie: FilmResponse): MovieFragment {

            // Store the movie data in a Bundle object
            val args = Bundle()
            args.putString(MovieHelper.KEY_TITLE, movie.title)
            args.putString(MovieHelper.KEY_RELEASE_DATE, movie.releaseDate)
            args.putString(MovieHelper.KEY_OPENING_CRAWL, movie.openingCrawl)

            // Create a new MovieFragment and set the Bundle as the arguments
            // to be retrieved and displayed when the view is created
            val fragment = MovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

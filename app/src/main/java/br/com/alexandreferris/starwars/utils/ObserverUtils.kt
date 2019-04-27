package br.com.alexandreferris.starwars.utils

import android.arch.lifecycle.Observer
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView

fun observeLoadingBar(loadingBar: ConstraintLayout): Observer<Boolean> = Observer {
    loadingBar.visibility = if (it!!) View.VISIBLE else View.GONE
}

fun observeLoadingError(textViewError: TextView): Observer<Boolean> = Observer {
    textViewError.visibility = if (it!!) View.VISIBLE else View.GONE
}
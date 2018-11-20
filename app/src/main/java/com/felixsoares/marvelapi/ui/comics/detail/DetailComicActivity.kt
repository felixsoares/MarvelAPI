package com.felixsoares.marvelapi.ui.comics.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.felixsoares.marvelapi.R
import com.felixsoares.marvelapi.data.repository.MarvelRepository
import com.felixsoares.marvelapi.databinding.ActivityDetailComicBinding
import com.felixsoares.marvelapi.ui.AppApplication
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_detail_comic.*

class DetailComicActivity : AppCompatActivity() {

    lateinit var viewModel: DetailComicViewModel
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val ID_COMIC = "ID_COMIC"

        fun newInstance(context: Context, idCharacter: String): Intent {
            return Intent(context, DetailComicActivity::class.java).apply {
                putExtra(ID_COMIC, idCharacter)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idComic = intent!!.extras!!.getString(ID_COMIC, "")
        val repository = MarvelRepository((application as AppApplication).service)

        viewModel = ViewModelProviders.of(this, DetailComicViewModel.Factory(repository, idComic))
            .get(DetailComicViewModel::class.java)

        val binding: ActivityDetailComicBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail_comic)
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        compositeDisposable.add(viewModel.urlField.subscribe { url ->
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.marvel)
                .error(R.drawable.marvel)
                .into(imgComic)
        })

        compositeDisposable.add(viewModel.showInternetError.subscribe {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        })
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}

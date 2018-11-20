package com.felixsoares.marvelapi.ui.characters.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.felixsoares.marvelapi.R
import com.felixsoares.marvelapi.data.repository.MarvelRepository
import com.felixsoares.marvelapi.data.utilities.UserInteraction
import com.felixsoares.marvelapi.databinding.ActivityDetailCharacterBinding
import com.felixsoares.marvelapi.ui.AppApplication
import com.felixsoares.marvelapi.ui.comics.detail.DetailComicActivity
import com.felixsoares.marvelapi.widget.ComicAdapter
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_detail_character.*

class DetailCharacterActivity : AppCompatActivity(), UserInteraction {

    lateinit var adapter: ComicAdapter
    lateinit var viewModel: DetailCharacterViewModel
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val ID_CHARACTER = "ID_CHARACTER"

        fun newInstance(context: Context, idCharacter: String): Intent {
            return Intent(context, DetailCharacterActivity::class.java).apply {
                putExtra(ID_CHARACTER, idCharacter)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idCharacter = intent!!.extras!!.getString(ID_CHARACTER, "")
        val repository = MarvelRepository((application as AppApplication).service)

        viewModel = ViewModelProviders.of(this, DetailCharacterViewModel.Factory(repository, idCharacter))
            .get(DetailCharacterViewModel::class.java)

        val binding: ActivityDetailCharacterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail_character)
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ComicAdapter(this, this)
        val layoutManager = LinearLayoutManager(this)

        recyclerView.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
            it.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        }
    }

    private fun setupViewModel() {
        compositeDisposable.add(viewModel.urlField.subscribe { url ->
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.marvel)
                .error(R.drawable.marvel)
                .into(imgChar)
        })

        compositeDisposable.add(viewModel.showInternetError.subscribe {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        })

        compositeDisposable.add(viewModel.comics.subscribe {
            adapter.addComics(it)
        })
    }

    override fun OnClick(idComic: String, shareView: View) {
        val detail = DetailComicActivity.newInstance(this, idComic)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            shareView,
            ViewCompat.getTransitionName(shareView)!!
        )

        startActivity(detail, options.toBundle())
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

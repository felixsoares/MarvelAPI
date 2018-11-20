package com.felixsoares.marvelapi.ui.characters.list

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felixsoares.marvelapi.R
import com.felixsoares.marvelapi.data.repository.MarvelRepository
import com.felixsoares.marvelapi.data.utilities.UserInteraction
import com.felixsoares.marvelapi.databinding.ActivityListCharacterBinding
import com.felixsoares.marvelapi.ui.AppApplication
import com.felixsoares.marvelapi.ui.characters.detail.DetailCharacterActivity
import com.felixsoares.marvelapi.widget.CharacterAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_list_character.*


class ListCharactersActivity : AppCompatActivity(), UserInteraction {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var viewModel: ListCharactersViewModel
    lateinit var adapter: CharacterAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MarvelRepository((application as AppApplication).service)

        viewModel = ViewModelProviders.of(this, ListCharactersViewModel.Factory(repository))
            .get(ListCharactersViewModel::class.java)

        val binding: ActivityListCharacterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_list_character)
        binding.viewModel = viewModel

        setupView()
        setupViewModel()

        viewModel.loadCharacters(false)
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.marvel)

        adapter = CharacterAdapter(this, this)
        layoutManager = LinearLayoutManager(this)
        recyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
            it.addOnScrollListener(recyclerViewOnScrollListener)
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.loadCharacters(false)
        }
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLoadingMore.get()!!) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE
                ) {
                    viewModel.loadCharacters(true)
                }
            }
        }
    }

    private fun setupViewModel() {
        compositeDisposable.add(viewModel.characters.subscribe {
            swipeRefresh.isRefreshing = false
            adapter.addCharacters(it)
        })

        compositeDisposable.add(viewModel.showInternetError.subscribe {
            swipeRefresh.isRefreshing = false
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        })
    }

    override fun OnClick(idCharacter: String, sharedView: View) {
        val detail = DetailCharacterActivity.newInstance(this, idCharacter)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            sharedView,
            ViewCompat.getTransitionName(sharedView)!!
        )

        startActivity(detail, options.toBundle())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}

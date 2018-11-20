package com.felixsoares.marvelapi.ui.comics.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.felixsoares.marvelapi.data.repository.IMarvelRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DetailComicViewModel(
    private val repository: IMarvelRepository,
    private val idComic: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val showInternetError = PublishSubject.create<Boolean>()
    val urlField = PublishSubject.create<String>()
    val titleField = ObservableField<String>()
    val descriptionField = ObservableField<String>()

    init {
        compositeDisposable.add(
            repository.getComic(idComic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ comic ->
                    titleField.set(comic.title)
                    descriptionField.set(comic.description.takeIf { it != "" } ?: "No description")
                    urlField.onNext(comic.thumbnail!!)
                }, {
                    showInternetError.onNext(true)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(
        private val repository: IMarvelRepository,
        private val idComic: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = DetailComicViewModel(repository, idComic) as T
    }
}
package com.felixsoares.marvelapi.ui.characters.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.felixsoares.marvelapi.data.database.RMComic
import com.felixsoares.marvelapi.data.repository.IMarvelRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DetailCharacterViewModel(
    repository: IMarvelRepository,
    idCharacter: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val urlField = PublishSubject.create<String>()
    val comics = PublishSubject.create<List<RMComic>>()
    val showInternetError = PublishSubject.create<Boolean>()
    val nameField = ObservableField<String>("")
    val descriptionField = ObservableField<String>("")

    init {
        compositeDisposable.add(
            repository.getCharacter(idCharacter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ char ->
                    nameField.set(char.name)
                    descriptionField.set(char.description.takeIf { it != "" } ?: "No description")
                    urlField.onNext(char.thumbnail!!)
                    comics.onNext(char.comics ?: mutableListOf())
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
        private val idCharacter: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            DetailCharacterViewModel(repository, idCharacter) as T
    }
}
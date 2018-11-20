package com.felixsoares.marvelapi.ui.characters.list

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.felixsoares.marvelapi.data.database.RMCharacter
import com.felixsoares.marvelapi.data.repository.IMarvelRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ListCharactersViewModel(
    private val repository: IMarvelRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val characters = PublishSubject.create<List<RMCharacter>>()
    val showInternetError = PublishSubject.create<Boolean>()
    val showProgress = ObservableField<Boolean>(false)
    val isLoadingMore = ObservableField<Boolean>(false)

    private var sizeOfList = 0

    fun loadCharacters(isFooterLoading: Boolean) {
        isLoadingMore.set(isFooterLoading)
        showProgress.set(!isFooterLoading)
        compositeDisposable.add(
            repository.getCharacters(sizeOfList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sizeOfList += it.size
                    characters.onNext(it)
                    resetObservables()
                }, {
                    resetObservables()
                    showInternetError.onNext(true)
                })
        )
    }

    private fun resetObservables() {
        isLoadingMore.set(false)
        showProgress.set(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(
        private val repository: IMarvelRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ListCharactersViewModel(repository) as T
    }

}
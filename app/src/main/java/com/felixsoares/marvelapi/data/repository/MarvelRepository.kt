package com.felixsoares.marvelapi.data.repository

import com.felixsoares.marvelapi.data.database.RMCharacter
import com.felixsoares.marvelapi.data.database.RMComic
import com.felixsoares.marvelapi.data.model.Character
import com.felixsoares.marvelapi.data.model.Comic
import com.felixsoares.marvelapi.data.service.IMarvelService
import com.felixsoares.marvelapi.data.utilities.Constants
import io.reactivex.Observable

interface IMarvelRepository {
    fun getCharacters(offset: Int): Observable<List<RMCharacter>>
    fun getCharacter(idCharacter: String): Observable<RMCharacter>
    fun getComic(idComic: String): Observable<RMComic>
}

class MarvelRepository(
    private val service: IMarvelService
) : IMarvelRepository {

    override fun getComic(idComic: String): Observable<RMComic> {
        return service.getComic(idComic)
            .filter { it.code == Constants.STATUS_CODE_OK }
            .map { response ->
                val result: Comic = response.data!!.results!![0]

                return@map RMComic().apply {
                    id = result.id
                    description = result.description
                    title = result.title
                    thumbnail = "${result.thumbnail!!.path}.${result.thumbnail!!.extension}"
                }
            }
    }

    override fun getCharacter(idCharacter: String): Observable<RMCharacter> {
        return service.getCharacter(idCharacter)
            .filter { it.code == Constants.STATUS_CODE_OK }
            .map { response ->
                val result: Character = response.data!!.results!![0]

                val comicList = mutableListOf<RMComic>()
                result.comics?.items?.forEach {
                    comicList.add(
                        RMComic().apply {
                            id = it.resourceURI?.replace("http://gateway.marvel.com/v1/public/comics/", "")
                            title = it.name
                        }
                    )
                }

                return@map RMCharacter().apply {
                    id = result.id
                    description = result.description
                    name = result.name
                    thumbnail = "${result.thumbnail!!.path}.${result.thumbnail!!.extension}"
                    comics = comicList
                }
            }
    }

    override fun getCharacters(offset: Int): Observable<List<RMCharacter>> {
        val characters = mutableListOf<RMCharacter>()
        return service.getCharacters(offset)
            .filter { it.code == Constants.STATUS_CODE_OK }
            .map { response ->
                response.data!!.results?.forEach { result ->
                    val char = RMCharacter().apply {
                        id = result.id
                        description = result.description
                        name = result.name
                        thumbnail = "${result.thumbnail!!.path}.${result.thumbnail!!.extension}"
                    }

                    characters.add(char)
                }

                return@map characters
            }
    }

}
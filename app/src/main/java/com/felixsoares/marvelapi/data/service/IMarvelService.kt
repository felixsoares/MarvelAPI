package com.felixsoares.marvelapi.data.service

import com.felixsoares.marvelapi.data.model.Character
import com.felixsoares.marvelapi.data.model.Comic
import com.felixsoares.marvelapi.data.model.Response
import com.felixsoares.marvelapi.data.utilities.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMarvelService {

    @GET("characters")
    fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 100,
        @Query("ts") timeStamp: String = Constants.TS,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.HASH
    ): Observable<Response<Character>>

    @GET("characters/{idCharacter}")
    fun getCharacter(
        @Path("idCharacter") idCharacter: String,
        @Query("ts") timeStamp: String = Constants.TS,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.HASH
    ): Observable<Response<Character>>

    @GET("comics")
    fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 100,
        @Query("ts") timeStamp: String = Constants.TS,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.HASH
    ): Observable<Response<Comic>>

    @GET("comics/{idComic}")
    fun getComic(
        @Path("idComic") idCharacter: String,
        @Query("ts") timeStamp: String = Constants.TS,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.HASH
    ): Observable<Response<Comic>>
}
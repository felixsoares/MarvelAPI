package com.felixsoares.marvelapi.data.model

class Response<T> {
    var code: Int? = null
    var data: Data<T>? = null
}
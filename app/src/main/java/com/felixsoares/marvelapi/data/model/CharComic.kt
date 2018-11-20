package com.felixsoares.marvelapi.data.model

class CharComic {
    var available: Int? = null
    var collectionURI: String? = null
    var items: List<Item>? = null

    inner class Item {
        var resourceURI: String? = null
        var name: String? = null
    }
}
package com.felixsoares.marvelapi.data.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class RMComic : RealmObject() {
    @PrimaryKey
    open var id: String? = null
    open var title: String? = null
    open var description: String? = null
    open var thumbnail: String? = null
    open var characters: List<RMCharacter>? = null
}
package com.felixsoares.marvelapi.ui

import android.app.Application
import com.felixsoares.marvelapi.data.service.IMarvelService
import com.felixsoares.marvelapi.data.service.ServiceGenerator
import io.realm.Realm

class AppApplication : Application() {

    lateinit var service: IMarvelService

    override fun onCreate() {
        super.onCreate()
        service = ServiceGenerator.createService(this)
        Realm.init(this)
    }

}
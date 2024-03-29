package com.example.letsdo

import android.app.Application
import com.example.letsdo.data.AppContainer
import com.example.letsdo.data.AppDataContainer

class ToDoApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
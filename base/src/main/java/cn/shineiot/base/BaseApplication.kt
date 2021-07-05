package cn.shineiot.base

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

open class BaseApplication : Application() {
    companion object {
        private lateinit var context: Context

        @JvmStatic
        fun context(): Context {
            return context
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}
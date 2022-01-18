package cn.shineiot.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

open class BaseApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
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
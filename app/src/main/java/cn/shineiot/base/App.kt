package cn.shineiot.base

import android.annotation.SuppressLint

class App : BaseApplication(){
    @SuppressLint("MissingSuperCall")
    override fun onCreate() {
        super.onCreate()
    }
}
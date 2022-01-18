package cn.shineiot.basic

import android.annotation.SuppressLint
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

class App : BaseApplication(){
    @SuppressLint("MissingSuperCall")
    override fun onCreate() {
        super.onCreate()

        setAutoSizeConfig()
    }

    private fun setAutoSizeConfig() {
        AutoSizeConfig.getInstance().unitsManager
            .setSupportDP(false).supportSubunits = Subunits.MM
    }
}
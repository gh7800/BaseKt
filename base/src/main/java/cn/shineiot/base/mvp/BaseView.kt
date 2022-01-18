package cn.shineiot.base.mvp

/**
 * @author Jake.Ho
 * created: 2017/10/25
 * desc:
 */
interface BaseView {

    fun showLoading()

    fun dismissLoading()

    fun errorMsg(msg: String?)

}

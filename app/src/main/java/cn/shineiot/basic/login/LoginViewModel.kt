package cn.shineiot.basic.login

import cn.shineiot.base.mvvm.BaseViewModel
import cn.shineiot.base.mvvm.Resource
import cn.shineiot.base.utils.LogUtil
import kotlinx.coroutines.flow.flow

/**
 * @Author : GF63
 * @Date : 2022/1/12 16:36
 */
class LoginViewModel(private var repository: LoginRepository) : BaseViewModel() {

    fun login(username : String,password : String,channelId : String,brand : String) = flow {
        emit(Resource.loading(null))
        try {
            val data = repository.login(username, password, channelId, brand).apiData()
            emit(Resource.success(data))
        }catch (e : Exception){
            emit(Resource.error(e))
        }
    }
}
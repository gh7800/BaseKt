package cn.shineiot.basic.login

import cn.shineiot.base.mvvm.BaseRepository
import cn.shineiot.basic.http.RetrofitClient

/**
 * @Author : GF63
 * @Date : 2022/3/17 15:22
 */
class LoginRepository : BaseRepository() {
    suspend fun login(
        username: String,
        password: String,
        channelId: String,
        brand: String
    ) =
        executeRequest {
            RetrofitClient.apiService.login(username, password, channelId, brand)
        }
}

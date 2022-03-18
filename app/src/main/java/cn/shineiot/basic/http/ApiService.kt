package cn.shineiot.basic.http

import cn.shineiot.basic.BuildConfig
import cn.shineiot.base.mvp.BaseResult
import cn.shineiot.basic.bean.UserBean
import retrofit2.http.*

/**
 * @Author : GF63
 * @Date : 2022/1/17 10:37
 */
interface ApiService {
    companion object {
        private const val BASE_URL = "http://tj.hwapp.site/"
        private const val TEST_URL = "http://tj.hwapp.site/"

        //BaseUrl
        fun getBaseUrl(): String {
            return when {
                BuildConfig.DEBUG -> {
                    TEST_URL
                }
                else -> {
                    BASE_URL
                }
            }
        }
    }

    //登录
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("channel_id") channel_id: String,
        @Query("mobile_brands") brand: String
    ): BaseResult<UserBean>

    //登出
    @GET("api/logout")
    suspend fun loginOut(): BaseResult<Any>


}


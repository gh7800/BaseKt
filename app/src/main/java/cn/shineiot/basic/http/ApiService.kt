package cn.shineiot.basic.http

import cn.shineiot.basic.BuildConfig
import cn.shineiot.base.mvp.BaseResult
import cn.shineiot.base.utils.Constants
import cn.shineiot.base.utils.MMKVUtil
import cn.shineiot.basic.bean.UserBean
import retrofit2.http.*

/**
 * @Author : GF63
 * @Date : 2022/1/17 10:37
 */
interface ApiService {
    companion object {
        private const val BASE_URL = "http://v8.hwapp.site/"
        private const val BASE_URL_SPARE = "http://tj.hwapp.site/"

        private const val TEST_URL = "http://v8.hwapp.site/"
        private const val TEST_URL_SPARE = "http://tj.hwapp.site/"

        /**
         * SERVE_BASE_URL 默认为false
         */
        fun getBaseUrl(): String {
            return when(MMKVUtil.getBoolean(Constants.SERVE_BASE_URL)) {
                false -> {
                    when {
                        BuildConfig.DEBUG -> {
                            TEST_URL
                        }
                        else -> {
                            BASE_URL
                        }
                    }
                }
                true ->{ //启用备用域名
                    when {
                        BuildConfig.DEBUG -> {
                            TEST_URL_SPARE
                        }
                        else -> {
                            BASE_URL_SPARE
                        }
                    }
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


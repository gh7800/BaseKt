package cn.shineiot.base.mvvm

import cn.shineiot.base.mvp.BaseListResult
import cn.shineiot.base.mvp.BaseResult

/**
 * @Description BaseRepository
 * @Author : GF63
 * @Date : 2022/3/17 15:01
 */
open class BaseRepository {
    suspend fun <T> executeRequest(request: suspend () -> BaseResult<T>): BaseResult<T> {
        return request()
    }

    suspend fun <T> executeRequestList(request: suspend () -> BaseListResult<T>): BaseListResult<T> {
        return request()
    }
}
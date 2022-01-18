package cn.shineiot.basic.mvp

data class BaseResult<T>(
    var data: T,
    var message: String,
    var success: Boolean
) {
    fun apiData(): T {

        if (success) {
            return data
        } else {
            throw ApiException(success, message)
        }
    }
}
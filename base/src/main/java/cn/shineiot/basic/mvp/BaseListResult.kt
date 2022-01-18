package cn.shineiot.basic.mvp

import cn.shineiot.basic.bean.Pagination

data class BaseListResult<T>(
        var data: MutableList<T>,
        var message: String,
        var success: Boolean,
        var pagination: Pagination?
) {
    fun apiData(): MutableList<T> {
        if (success) {
            return data
        } else {
            throw ApiException(success, message)
        }
    }

    fun apiPagination(): Pagination {
            return pagination!!
    }
}
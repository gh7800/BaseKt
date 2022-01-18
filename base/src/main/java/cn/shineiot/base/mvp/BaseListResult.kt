package cn.shineiot.base.mvp

import cn.shineiot.base.bean.Pagination

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
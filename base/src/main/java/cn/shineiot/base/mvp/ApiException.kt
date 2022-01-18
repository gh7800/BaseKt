package cn.shineiot.base.mvp

class ApiException(var code: Boolean, override var message: String) : RuntimeException()
package cn.shineiot.basic.mvp

class ApiException(var code: Boolean, override var message: String) : RuntimeException()
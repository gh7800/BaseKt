package cn.shineiot.base.adapter

interface IDiff {
    infix fun diff(other: Any?): Any?
}
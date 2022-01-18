package cn.shineiot.basic.adapter

interface IDiff {
    infix fun diff(other: Any?): Any?
}
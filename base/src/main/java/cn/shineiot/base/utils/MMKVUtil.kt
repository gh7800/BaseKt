package cn.shineiot.base.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * @Description mmkv
 * @Author : GF63
 * @Date : 2022/3/18 14:44
 */
object MMKVUtil {
    private val kv by lazy { MMKV.defaultMMKV() }

    /**保存kv数据*/
    fun <T> save(key: String, value: T) {
        when (value) {
            is Int -> kv.encode(key, value)
            is Long -> kv.encode(key, value)
            is String -> kv.encode(key, value)
            is Boolean -> kv.encode(key, value)
            is Float -> kv.encode(key, value)
            is Double -> kv.encode(key, value)
            is Parcelable -> kv.encode(key, value)
        }
    }

    fun getString(key: String): String? {
        return kv.decodeString(key, null)
    }

    fun getInt(key: String): Int {
        return kv.decodeInt(key, 0)
    }

    fun getLong(key: String): Long {
        return kv.decodeLong(key, 0)
    }

    fun getFloat(key: String): Float {
        return kv.decodeFloat(key, 0F)
    }

    fun getDouble(key: String): Double {
        return kv.decodeDouble(key, 0.0)
    }

    /**获取序列化的对象*/
    fun <T : Parcelable> getParcelable(key: String, cls: Class<T>): T? {
        return kv.decodeParcelable(key, cls)
    }

    /**删除key*/
    fun deleteKey(key: String): Boolean {
        val isContain = kv.containsKey(key)
        return if (isContain) {
            kv.removeValueForKey(key)
            return kv.containsKey(key)
        } else {
            false
        }
    }

    /**
     * 删除多个key
     * val list = arrayOf("1","2")
     */
    fun deleteKeyList(key: Array<String>) {
        kv.removeValuesForKeys(key)
    }

    /**是否包含key*/
    fun isContain(key: String) : Boolean{
        return kv.containsKey(key)
    }
}
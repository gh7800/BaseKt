package cn.shineiot.basic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Author : GF63
 * @Date : 2022/3/16 13:27
 */
@Parcelize
data class UserBean(
    var name : String
):Parcelable

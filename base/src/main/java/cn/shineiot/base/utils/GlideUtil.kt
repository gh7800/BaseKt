package cn.shineiot.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import cn.shineiot.base.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

object GlideUtil {

    //正常
    @SuppressLint("ResourceAsColor")
    fun loadFile(context: Context, url: Any, view: ImageView) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(view)
    }

    fun loadFileLocal(context: Context, url: String, view: ImageView){
        Glide.with(context)
            //.asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(view)
    }

    //矩形圆角
    @SuppressLint("CheckResult")
    fun loadFileRectangle(context: Context, url: Any, view: AppCompatImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.icon_error_photo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(view)
    }

    //圆形图片
    fun loadFileCircle(context: Context, url: Any, view: AppCompatImageView) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transition(withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(view)
    }

}
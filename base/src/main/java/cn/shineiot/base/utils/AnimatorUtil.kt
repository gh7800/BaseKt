package cn.shineiot.base.utils

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * 动画工具
 */
object AnimatorUtil {

    //旋转90°
    fun startRotate(view: View) {
        val animator = view.animate()
        animator.rotation(90f)
        animator.duration = 300
        animator.start()
    }

    //回到原点
    fun endRotate(view: View) {
        val animator = view.animate()
        animator.rotation(0f)
        animator.duration = 300
        animator.start()
    }

    /**
     * 关闭默认局部刷新动画
     */
    fun closeDefaultAnimator(recyclerView: RecyclerView) {
        recyclerView.itemAnimator?.run {
            addDuration = 100
            moveDuration = 0
            removeDuration = 0
            (this as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}
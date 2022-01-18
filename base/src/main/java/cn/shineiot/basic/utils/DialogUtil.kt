package cn.shineiot.basic.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import cn.shineiot.basic.R
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.maning.mndialoglibrary.MProgressDialog
import com.maning.mndialoglibrary.MStatusDialog
import com.maning.mndialoglibrary.config.MDialogConfig
import com.maning.mndialoglibrary.listeners.OnDialogDismissListener

object DialogUtil {
    private val mBuild by lazy { MDialogConfig.Builder() }
    private var dialog: MaterialDialog? = null

    /**
     * loading
     */
    fun showLoading(
        context: Context,
        content: String? = "加载中"
    ) {
        mBuild.isCancelable(true)
            .isCanceledOnTouchOutside(false)
        val mConfig = mBuild.build()
        MProgressDialog.showProgress(context, content, mConfig)
    }

    /**
     * @param onDialogDismissListener dialog关闭监听
     */
    fun showLoading(
        context: Context,
        content: String,
        onDialogDismissListener: OnDialogDismissListener
    ) {
        mBuild.isCancelable(true)
            .isCanceledOnTouchOutside(false)
            .setOnDialogDismissListener(onDialogDismissListener)
        val mConfig = mBuild.build()
        MProgressDialog.showProgress(context, content, mConfig)
    }

    fun hideDialog() {
        MProgressDialog.dismissProgress()
    }

    /**
     * MaterialDialog
     *
     * cancelBack　取消回调
     * sureBack 确定回调
     * owner 是否绑定生命周期
     */
    fun showMaterialDialog(
        context: AppCompatActivity,
        msg: String,
        titleStr: String? = null,
        positiveBt: String? = "确定",
        negativeBt: String? = "取消",
        cancelOnTouch: Boolean = true,
        sureBack: DialogCallback? = null,
        cancelBack: DialogCallback? = null,
        drawable: Int? = 0,
    ) {
        dialog = MaterialDialog(context).show {
            if (drawable != null && drawable > 0) {
                icon(drawable)
            }
            cancelOnTouchOutside(cancelOnTouch)
            cancelable(true)
            message(0, msg)
            lifecycleOwner(context)

            if (null != titleStr) {
                title(0, titleStr)
            }

            positiveButton(0, positiveBt) {
                if (null != sureBack) {
                    it.show(sureBack)
                }
            }
            negativeButton(0, negativeBt) {
                if (null != cancelBack) {
                    it.show(cancelBack)
                }
            }
            onDismiss {
                dialog?.dismiss()
                dialog = null
            }
        }
    }

    /**
     * 带图片的，提交成功
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun showSuccess(context: AppCompatActivity, msg: String? = "提交成功", delay : Long = 2000){
        if(msg?.isNotEmpty() == true){
            val dialog = MStatusDialog(context)
            dialog.show(msg, context.getDrawable(R.drawable.icon_success),delay)
        }
    }

    /**
     * 带图片的，加载失败
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun showError(context: AppCompatActivity, msg: String?= "加载失败", delay : Long = 2000){
        if(msg?.isNotEmpty() == true){
            val dialog = MStatusDialog(context)
            dialog.show(msg, context.getDrawable(R.drawable.icon_error),delay)
        }
    }
}
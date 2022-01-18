package cn.shineiot.base.manager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File

object FileManger {

    //App包下文件路径 packageName/files
    fun getPackagePath(context: Context, fileDir: String = ""): String {
        val file = context.getExternalFilesDir("")
        if (!file?.exists()!!) {
            file.mkdirs()
        }
        if (!TextUtils.isEmpty(fileDir)) {
            val dirFile = file.absolutePath + "/" + fileDir
            val fileDir = File(dirFile)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            return fileDir.absolutePath + "/"
        }

        return file.absolutePath
    }

    //App包下缓存路径  packageName/cache
    fun getPackagePathCache(context: Context, fileDir: String = ""): String {
        val file = context.externalCacheDir
        if (!file?.exists()!!) {
            file.mkdirs()
        }
        if (!TextUtils.isEmpty(fileDir)) {
            val dirFile = file.absolutePath + "/" + fileDir
            val fileDir = File(dirFile)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            return fileDir.absolutePath + "/"
        }

        return file.absolutePath
    }

    //SD卡根路径
    @Deprecated("Android 10 已废弃")
    fun getSdcardPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun deleteFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!file.isDirectory) {
            file.delete()
        } else {
            if (null != file.listFiles()) {
                file.listFiles().forEach {
                    deleteFile(it)
                }
            }
        }
        return true
    }


    //文件夹大小
    fun getFileSize(file: File?): Long {
        if (file == null) return -1
        var size: Long = 0
        if (!file.isDirectory) {
            size = file.length()
        } else {
            if (null != file.listFiles() && file.listFiles().size > 0) {
                file.listFiles().forEach {
                    size += getFileSize(it)
                }
            }
        }
        return size
    }

    private const val ONE_KB_SIZE = 1024
    private const val ONE_MB_SIZE = 1024 * 1024
    private const val ONE_GB_SIZE = 1024 * 1024 * 1024

    private const val BT_TAG = "B"
    private const val KB_TAG = "K"
    private const val MB_TAG = "M"
    private const val GB_TAG = "G"

    // 把文件大小转化成容易理解的格式显示，如多少M
    @SuppressLint("DefaultLocale")
    fun sizeToString(size: Long): String {
        var str = ""
        str = if (size in 0 until ONE_KB_SIZE) {
            size.toString() + BT_TAG
        } else if (size in ONE_KB_SIZE until ONE_MB_SIZE) {
            java.lang.String.format("%.1f", size.toFloat() / ONE_KB_SIZE) + KB_TAG
        } else if (size in ONE_MB_SIZE until ONE_GB_SIZE) {
            java.lang.String.format("%.1f", size.toFloat() / ONE_MB_SIZE) + MB_TAG
        } else {
            java.lang.String.format("%.1f", size.toFloat() / ONE_GB_SIZE) + GB_TAG
        }
        return str
    }

    //获取文件类型
    fun getFileType(path: String?): String {
        return if (!path.isNullOrEmpty()) {
            val index = path.lastIndexOf(".") + 1
            path.substring(index, path.length)
        } else {
            ""
        }
    }

    /**
     * 是否是图片类型
     */
    fun isImageType(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            false
        } else {
            val index = path.lastIndexOf(".") + 1
            when (path.substring(index, path.length)) {
                "png" -> return true
                "PNG" -> return true
                "jpg" -> return true
                "jpeg" -> return true
                "JPEG" -> return true
                "webp" -> return true
                else -> return false
            }
            false
        }
        return false
    }

    /**
     * 获取文件名
     */
    fun getFileName(path : String?): String{
        return if(!path.isNullOrEmpty()){
            val index = path.lastIndexOf("/")
            path.substring(index+1,path.length)
        }else{
            ""
        }
    }



}
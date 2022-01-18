package cn.shineiot.basic.utils

interface ProgressListener {
    /**
     * 下载进度
     */
    fun onProgress(progress: Int,alreadyDownloadLength : Long)

    /**
     * 下载完成
     */
    fun onDone(totalSize: Long)
}
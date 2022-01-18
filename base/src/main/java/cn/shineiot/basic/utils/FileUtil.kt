package cn.shineiot.basic.utils

import cn.shineiot.basic.manager.DownProgressListener
import okhttp3.ResponseBody
import java.io.*

/**
 * 文件写入工具
 * 支持断点输入
 */
object FileUtil {

    /**
     * @param downLength 已下载的长度
     */
    fun writeToFile(
        body: ResponseBody,
        savePath: String,
        downLength: Long = 0L,
        progressListener: DownProgressListener? = null
    ) {

        val index = savePath.lastIndexOf("/")
        val filePath = savePath.substring(0, index)
        //val fileName = savePath.substring(index, savePath.length)
        var randomAccessFile: RandomAccessFile?

        val dir = File(filePath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file_length = body.contentLength() + downLength

        if (file_length <= 0) {
            return
        }
        val inPutStream = body.byteStream()
        //var fos: FileOutputStream? = null
        //val result = File(dir, fileName)

        var totalLength = downLength

        LogUtil.e("$downLength $file_length $totalLength downLength")

        randomAccessFile = RandomAccessFile(savePath, "rw")
        if(randomAccessFile.length() == 0L) {
            randomAccessFile.setLength(file_length)
        }
        randomAccessFile.seek(downLength)


        var len: Int
        val buf = ByteArray(1024 * 8)

        try {
            //fos = FileOutputStream(result)
            while (inPutStream.read(buf).also { len = it } != -1) {
                totalLength += len

                val progress = ((totalLength + downLength) / file_length.toFloat() * 100).toInt()

                progressListener?.downProgress(progress)

                randomAccessFile.write(buf, 0, len)
            }
            //fos.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            LogUtil.e(e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtil.e(e.message)
        }


        try {
            inPutStream.close()
        } catch (ignored: IOException) {
            ignored.stackTrace
        }

        try {
            //fos!!.close()
            randomAccessFile.close()
        } catch (ignored: IOException) {
            ignored.stackTrace
        }
    }

}
package cn.shineiot.basic.utils

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author gf63
 * 支持断点下载
 */
class ProgressResponseBody(
    private val mResponseBody: ResponseBody,
    private val downLength : Long ,
    private val mProgressListener: ProgressListener
) :ResponseBody() {

    private var mBufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return mResponseBody.contentType()
    }

    override fun contentLength(): Long {
        return mResponseBody.contentLength() + downLength
    }

    override fun source(): BufferedSource {
        if (mBufferedSource == null) {
            mBufferedSource = getSource(mResponseBody.source()).buffer()
        }
        return mBufferedSource as BufferedSource
    }

    private fun getSource(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalSize = 0L
            var sum = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                if (totalSize == 0L) {
                    totalSize = contentLength()
                }
                LogUtil.e("$downLength totalSize==")
                val len = super.read(sink, byteCount)
                sum += if (len == -1L) 0 else len
                val progress = ((sum + downLength) * 1.0f / totalSize * 100).toInt()

                if (len == -1L) {
                    mProgressListener.onDone(totalSize)
                } else {
                    mProgressListener.onProgress(progress,sum + downLength)
                }
                return len
            }
        }
    }

}
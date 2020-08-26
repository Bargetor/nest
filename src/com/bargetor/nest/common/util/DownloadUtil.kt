package com.bargetor.nest.common.util

import java.io.IOException
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel



class DownloadUtil{
    // Using NIO
    companion object {
        fun downloadFileFromURLUsingNIO(fileName: String, fileUrl: String) {
            try {
                val url = URL(fileUrl)
                val readableByteChannel = Channels.newChannel(url.openStream())
                val fileOutputStream = FileOutputStream(fileName)
                fileOutputStream.channel.transferFrom(readableByteChannel, 0, java.lang.Long.MAX_VALUE)
                fileOutputStream.close()
                readableByteChannel.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}
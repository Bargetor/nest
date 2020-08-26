package com.bargetor.nest.common.util

import java.util.*

class FileUtil {
    companion object {
        fun getTempDirPath(): String{
            var tempPath = System.getProperty("java.io.tmpdir")
            if (!tempPath.endsWith("/")) tempPath += "/"
            return tempPath
        }

        fun getTempFileName(prefix: String, fileExtName: String): String{
            return "%s%s_%s.%s".format(
                    this.getTempDirPath(),
                    prefix,
                    UUID.randomUUID().toString(),
                    fileExtName
            )
        }
    }
}
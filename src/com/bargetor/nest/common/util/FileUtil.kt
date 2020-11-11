package com.bargetor.nest.common.util

import java.util.*

class FileUtil {
    companion object {
        fun isWindowsOS(): Boolean{
            return System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0
        }

        fun getTempDirPath(): String{
            var tempPath = System.getProperty("java.io.tmpdir")
            if (!tempPath.endsWith("/") && !isWindowsOS()) tempPath += "/"
            if (!tempPath.endsWith("/") && isWindowsOS()) tempPath += "\\"
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
package com.bargetor.nest.common.util

class UnicodeUtil{
    companion object {
        fun unicode(string: String): String{
            return unicodeForList(string).joinToString("")
        }

        fun unicodeForList(string: String): List<String>{
            return unicodeForListWithoutU(string).map {
                // 转换为unicode
                "\\u" + it
            }
        }

        fun unicodeForListWithoutU(string: String): List<String>{
            return string.toCharArray().map {
                // 转换为unicode
                Integer.toHexString(it.toInt())
            }
        }

        fun deUnicode(unicode: String): String{
            val string = StringBuffer()
            unicode.split("\\u").forEach {
                if (it.isEmpty()) return@forEach
                // 转换出每一个代码点
                val data = Integer.parseInt(it, 16)
                // 追加成string
                string.append(data.toChar())
            }
            return string.toString()
        }
    }
}

fun main(args: Array<String>) {
    println(UnicodeUtil.unicode("上海"))
    println(UnicodeUtil.deUnicode("\\u4e0a\\u6d77"))
}
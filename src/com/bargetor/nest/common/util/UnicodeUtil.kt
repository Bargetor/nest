package com.bargetor.nest.common.util

import java.util.regex.Pattern

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


        fun unicodeToString(str: String): String {
            var str = str

            val pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))")
            val matcher = pattern.matcher(str)
            var ch: Char
            while (matcher.find()) {
                ch = Integer.parseInt(matcher.group(2), 16).toChar()
                str = str.replace(matcher.group(1), ch + "")
            }
            return str
        }
    }
}

fun main(args: Array<String>) {
    println(UnicodeUtil.unicode("上海"))
    println(UnicodeUtil.unicodeToString("\\u4e0aFF\\u6d77"))
}
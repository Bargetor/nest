package com.bargetor.nest.common.util

import java.util.regex.Pattern

@kotlin.jvm.JvmName("systemPrintRunTime")
public fun <T>printRunTime(action: () -> T): T {
    val start = System.currentTimeMillis()
    val result = action()
    println("run time:" + (System.currentTimeMillis() - start))
    return result
}

@kotlin.jvm.JvmName("filterEmoji")
public fun String.filterEmoji(): String {
    if (this.trim().isEmpty()) {
        return this
    }
    val pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]"
    val reStr = ""
    val emoji = Pattern.compile(pattern)
    val emojiMatcher = emoji.matcher(this)
    return emojiMatcher.replaceAll(reStr)
}
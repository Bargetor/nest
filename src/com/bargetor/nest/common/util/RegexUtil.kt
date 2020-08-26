package com.bargetor.nest.common.util

import java.util.regex.Pattern

class RegexUtil {
    companion object {
        private val double_pattern = Regex("[.\\d]+")

        fun findDouble(string: String): List<Double> {
            val doubleString = find(double_pattern, string)
            return doubleString.filter { StringUtil.isNotNullStr(it) }.map { it.toDouble() }
        }

        fun find(regex: String, string: String): List<String> {
            val regex = Regex(regex)
            return find(regex, string)
        }

        fun find(regex: Regex, string: String, groupIndex: Int? = null): List<String> {
            val matcher = regex.findAll(string)
            return matcher.map {
                if (groupIndex != null) it.groups[groupIndex]!!.value else it.value
            }.toList()
        }
    }
}

fun main(args: Array<String>) {
    println(RegexUtil.findDouble("分红：每份派现金0.01元0.02"))
}
package com.bargetor.nest.common.util

import java.math.BigDecimal

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
public inline fun <T> Iterable<T>.sumByBigDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum: BigDecimal = BigDecimal.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun main(args: Array<String>) {
    println(BigDecimal.ONE / BigDecimal.ZERO)
}
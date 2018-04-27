package com.bargetor.nest.common.util

import com.google.common.collect.Range

/**
 * range转换
 */
@kotlin.jvm.JvmName("transformOfRange")
public fun <C: Comparable<C>, M: Comparable<M>>Range<C>.transform(transform: (point: C, isLower: Boolean) -> M): Range<M> {
    return transformRange(this, transform)
}

public fun <C: Comparable<C>, M: Comparable<M>>transformRange(range: Range<C>, transform: (point: C, isLower: Boolean) -> M): Range<M>{
    val newLower = if (range.hasLowerBound()) transform(range.lowerEndpoint(), true) else null
    val newUpper = if (range.hasUpperBound()) transform(range.upperEndpoint(), false) else null
    if (newLower == null && newUpper == null) return Range.closed(newLower, newUpper)
    if (newLower == null && newUpper != null) return Range.atMost(newUpper)
    if (newLower != null && newUpper == null) return Range.atLeast(newLower)
    return Range.closed(newLower, newUpper)
}
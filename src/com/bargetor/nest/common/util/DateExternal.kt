package com.bargetor.nest.common.util

import java.util.*

public inline fun Date.dayStart(): Date{
    return DateUtil.getDayStart(this)
}

public inline fun Date.dayEnd(): Date{
    return DateUtil.getDayEnd(this)
}
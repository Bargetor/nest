package com.bargetor.nest.common.util

import java.math.BigDecimal

public inline fun BigDecimal.scale2HalfUp(): BigDecimal{
    return this.setScale(2, BigDecimal.ROUND_HALF_UP)
}
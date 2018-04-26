package com.bargetor.nest.mybatis

import java.io.Serializable
import java.math.BigInteger

class SelectRange : Serializable{
    var start: BigInteger = BigInteger.ZERO
    var end: BigInteger = BigInteger.ZERO
    constructor()
    constructor(start: BigInteger, end: BigInteger){
        this.start = start
        this.end = end
    }

    fun isInvalid(): Boolean {
        return this.start >= this.end || this.start < BigInteger.ZERO || this.end < BigInteger.ZERO
    }

    override fun toString(): String {
        return "SelectRange(start=$start, end=$end)"
    }


    companion object {
        fun build(start: BigInteger, count: BigInteger): SelectRange {
            return SelectRange(start, start + count - BigInteger.ONE)
        }
    }
}
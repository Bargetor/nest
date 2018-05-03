package com.bargetor.nest.mybatis.handler

import org.apache.ibatis.type.EnumTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

@MappedJdbcTypes(JdbcType.TINYINT, JdbcType.INTEGER, JdbcType.BIGINT, JdbcType.SMALLINT)
@MappedTypes(CodeEnum::class)
class EnumTypeHandler<E : Enum<E>>(val type: Class<E>) : EnumTypeHandler<E>(type){

    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: E, jdbcType: JdbcType?) {
        if (CodeEnum::class.java.isAssignableFrom(this.type)){
            val codeEnum = parameter as CodeEnum<E>
            if (jdbcType == null) {
                ps?.setInt(i, codeEnum.getCode())
            } else {
                ps?.setObject(i, codeEnum.getCode(), jdbcType.TYPE_CODE) // see r3589
            }
        }else{
            super.setNonNullParameter(ps, i, parameter, jdbcType)
        }
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): E? {
        if (CodeEnum::class.java.isAssignableFrom(this.type)){
            val c = cs?.getInt(columnIndex) ?: return null
            return CodeEnum.valueOfCode(this.type as Class<CodeEnum<E>>, c) as E
        }else{
            return super.getNullableResult(cs, columnIndex)
        }
    }

    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): E? {
        if (CodeEnum::class.java.isAssignableFrom(this.type)){
            val c = rs?.getInt(columnIndex) ?: return null
            return CodeEnum.valueOfCode(this.type as Class<CodeEnum<E>>, c) as E
        }else{
            return super.getNullableResult(rs, columnIndex)
        }
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): E? {
        if (CodeEnum::class.java.isAssignableFrom(this.type)){
            val c = rs?.getInt(columnName) ?: return null
            return CodeEnum.valueOfCode(this.type as Class<CodeEnum<E>>, c) as E
        }else{
            return super.getNullableResult(rs, columnName)
        }
    }
}

interface CodeEnum<E : Enum<E>>{
    fun getCode(): Int

    companion object {
        inline fun <E : Enum<E>>valueOfCode(codeEnumType: Class<CodeEnum<E>>, code: Int): CodeEnum<E>?{
            return codeEnumType.enumConstants.firstOrNull {
                val e = it as CodeEnum<E>
                e.getCode() == code
            }
        }

    }
}
package com.ryulth.worklifebell.api.util

import com.ryulth.worklifebell.api.exception.DateFormatException
import java.util.regex.Pattern

object DateFormatUtils {
    private val dateTimePattern: Pattern = Pattern.compile(
        "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})",
        Pattern.CASE_INSENSITIVE
    )

    fun validateDateTime(string: String): Boolean{
        if (dateTimePattern.matcher(string).matches()) return true
        throw DateFormatException("Datetime format exception")
    }
}
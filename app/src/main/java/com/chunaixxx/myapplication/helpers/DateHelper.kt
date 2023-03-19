package com.chunaixxx.myapplication.helpers

import java.time.LocalDate

class DateHelper {
    fun getCurrentDate(): String{
        val currentDate = LocalDate.now().toString()
        val splitStr = currentDate.split("-")
        val day = splitStr[2]
        val year = splitStr[0]
        val month = when(splitStr[1]){
            "01" -> "Янв"
            "02" -> "Фев"
            "03" -> "Мар"
            "04" -> "Апр"
            "05" -> "Май"
            "06" -> "Июн"
            "07" -> "Июл"
            "08" -> "Авг"
            "09" -> "Сен"
            "10" -> "Окт"
            "11" -> "Ноя"
            "12" -> "Дек"
            else -> {"undefined"}
        }
        return "$day $month, $year"
    }
}
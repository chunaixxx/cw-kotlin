package com.chunaixxx.myapplication.Color

import com.chunaixxx.myapplication.R

open class ColorData {
    val colors = mutableListOf<ColorObject>(
        ColorObject("Синий", R.color.blue_default),
        ColorObject("Красный",R.color.red),
        ColorObject("Желтый",R.color.yellow),
        ColorObject("Зеленый",R.color.green_bg)
    )
}
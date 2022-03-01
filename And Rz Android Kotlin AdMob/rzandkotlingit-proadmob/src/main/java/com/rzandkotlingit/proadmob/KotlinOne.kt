package com.rzandkotlingit.proadmob

import java.text.DecimalFormat
import java.util.Random

fun getRandomId(from: Int, to: Int): Int {
    val random = Random()
    return random.nextInt(to - from) + from
}

fun getRandomFloat(from: Double, to: Double): Double {
    val random = Random()
    return getDecimalFormat(random.nextFloat() * (to - from) + from)
}

fun getDecimalFormat(value: Double): Double {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value).toDouble()
}
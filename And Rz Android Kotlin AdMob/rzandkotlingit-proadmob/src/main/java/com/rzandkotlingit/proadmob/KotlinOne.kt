package com.rzandkotlingit.proadmob

import java.util.Random

fun getRandomId(from: Int, to: Int): Int {
    val random = Random()
    return random.nextInt(to - from) + from
}
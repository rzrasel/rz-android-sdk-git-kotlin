package com.rzandkotlingit.core

import java.util.Random

fun randomId(from: Int, to: Int): Int {
    val random = Random()
    return random.nextInt(to - from) + from
}
package com.abbosidev.infrastructure.util

fun String.removePrePlus() = if (this.first() == '+') this.drop(1) else this

package com.feature.autentication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
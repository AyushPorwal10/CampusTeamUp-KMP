package com.campus.teamup

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
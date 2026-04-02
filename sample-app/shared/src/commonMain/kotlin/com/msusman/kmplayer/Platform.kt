package com.msusman.kmplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
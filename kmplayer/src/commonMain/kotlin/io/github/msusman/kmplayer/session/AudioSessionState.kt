package io.github.msusman.kmplayer.session

enum class AudioSessionState {
    GRANTED,
    TRANSIENT_LOSS,
    DUCK,
    LOST
}

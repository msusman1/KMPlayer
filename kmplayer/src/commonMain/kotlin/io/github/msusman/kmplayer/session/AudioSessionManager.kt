package io.github.msusman.kmplayer.session

interface AudioSessionManager {
    fun requestFocus(): AudioSessionState
    fun abandonFocus()
    fun currentState(): AudioSessionState
    fun release()
}

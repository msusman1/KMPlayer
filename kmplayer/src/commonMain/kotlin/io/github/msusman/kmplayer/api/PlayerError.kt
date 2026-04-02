package io.github.msusman.kmplayer.api

sealed class PlayerError {
    data class SourceUnavailable(val uri: String, val message: String? = null) : PlayerError()
    data class DecodeFailure(val message: String? = null) : PlayerError()
    data class NetworkFailure(val message: String? = null) : PlayerError()
    data class CacheFailure(val message: String? = null) : PlayerError()
    data class AudioFocusLost(val message: String? = null) : PlayerError()
    data class Unknown(val message: String? = null, val throwable: Throwable? = null) : PlayerError()
}

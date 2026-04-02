package com.kmplayer.player

class PlayerController(
    private val engine: PlaybackEngine
) {
    private var playlist: List<Track> = emptyList()
    private var index: Int = -1

    var state: PlaybackState = PlaybackState.IDLE
        private set

    val currentTrack: Track?
        get() = if (index in playlist.indices) playlist[index] else null

    fun setPlaylist(tracks: List<Track>, startIndex: Int = 0): Boolean {
        if (tracks.isEmpty()) {
            playlist = emptyList()
            index = -1
            state = PlaybackState.IDLE
            return false
        }
        val safeIndex = startIndex.coerceIn(0, tracks.lastIndex)
        playlist = tracks
        index = safeIndex
        return prepareCurrent()
    }

    fun play(): Boolean {
        return when (state) {
            PlaybackState.READY, PlaybackState.PAUSED -> {
                engine.play()
                state = PlaybackState.PLAYING
                true
            }
            PlaybackState.PLAYING -> true
            else -> false
        }
    }

    fun pause(): Boolean {
        return if (state == PlaybackState.PLAYING) {
            engine.pause()
            state = PlaybackState.PAUSED
            true
        } else {
            false
        }
    }

    fun stop(): Boolean {
        return if (state != PlaybackState.IDLE) {
            engine.stop()
            state = PlaybackState.STOPPED
            true
        } else {
            false
        }
    }

    fun next(): Boolean {
        if (playlist.isEmpty()) return false
        val nextIndex = index + 1
        return if (nextIndex > playlist.lastIndex) {
            engine.stop()
            state = PlaybackState.ENDED
            false
        } else {
            index = nextIndex
            prepareCurrent()
        }
    }

    fun previous(): Boolean {
        if (playlist.isEmpty()) return false
        val prevIndex = index - 1
        return if (prevIndex < 0) {
            false
        } else {
            index = prevIndex
            prepareCurrent()
        }
    }

    fun release() {
        engine.release()
        state = PlaybackState.IDLE
        playlist = emptyList()
        index = -1
    }

    private fun prepareCurrent(): Boolean {
        val track = currentTrack ?: return false
        return if (engine.prepare(track)) {
            state = PlaybackState.READY
            true
        } else {
            state = PlaybackState.ERROR
            false
        }
    }
}

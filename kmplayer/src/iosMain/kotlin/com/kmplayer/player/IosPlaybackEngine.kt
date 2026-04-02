package com.kmplayer.player

internal class IosPlaybackEngine : PlaybackEngine {
    override val isPlaying: Boolean
        get() = false

    override fun prepare(track: Track): Boolean = true

    override fun play() {
        // TODO: Implement with AVAudioPlayer/AVPlayer.
    }

    override fun pause() {
        // TODO: Implement with AVAudioPlayer/AVPlayer.
    }

    override fun stop() {
        // TODO: Implement with AVAudioPlayer/AVPlayer.
    }

    override fun release() {
        // TODO: Implement with AVAudioPlayer/AVPlayer.
    }
}

actual fun createPlaybackEngine(): PlaybackEngine = IosPlaybackEngine()

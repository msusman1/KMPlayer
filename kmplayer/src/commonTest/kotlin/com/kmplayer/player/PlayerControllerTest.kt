package com.kmplayer.player

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerControllerTest {
    private val trackA = Track(id = "a", url = "https://example.com/a.mp3", title = "A")
    private val trackB = Track(id = "b", url = "https://example.com/b.mp3", title = "B")

    @Test
    fun setPlaylistPreparesFirstTrack() {
        val engine = FakePlaybackEngine()
        val controller = PlayerController(engine)

        val prepared = controller.setPlaylist(listOf(trackA, trackB))

        assertTrue(prepared)
        assertEquals(PlaybackState.READY, controller.state)
        assertEquals(trackA, controller.currentTrack)
        assertEquals(trackA, engine.preparedTrack)
    }

    @Test
    fun playPauseTransitions() {
        val engine = FakePlaybackEngine()
        val controller = PlayerController(engine)

        controller.setPlaylist(listOf(trackA))

        assertTrue(controller.play())
        assertEquals(PlaybackState.PLAYING, controller.state)

        assertTrue(controller.pause())
        assertEquals(PlaybackState.PAUSED, controller.state)
    }

    @Test
    fun nextPreviousNavigation() {
        val engine = FakePlaybackEngine()
        val controller = PlayerController(engine)

        controller.setPlaylist(listOf(trackA, trackB))
        assertTrue(controller.next())
        assertEquals(trackB, controller.currentTrack)

        assertTrue(controller.previous())
        assertEquals(trackA, controller.currentTrack)
    }

    @Test
    fun endOfPlaylistSetsEndedState() {
        val engine = FakePlaybackEngine()
        val controller = PlayerController(engine)

        controller.setPlaylist(listOf(trackA))

        assertFalse(controller.next())
        assertEquals(PlaybackState.ENDED, controller.state)
    }
}

private class FakePlaybackEngine : PlaybackEngine {
    var preparedTrack: Track? = null
    private var playing: Boolean = false

    override val isPlaying: Boolean
        get() = playing

    override fun prepare(track: Track): Boolean {
        preparedTrack = track
        playing = false
        return true
    }

    override fun play() {
        playing = true
    }

    override fun pause() {
        playing = false
    }

    override fun stop() {
        playing = false
    }

    override fun release() {
        playing = false
    }
}

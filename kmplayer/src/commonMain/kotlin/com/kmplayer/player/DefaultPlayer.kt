package com.kmplayer.player

class DefaultPlayer(
    engine: PlaybackEngine = createPlaybackEngine()
) {
    val controller: PlayerController = PlayerController(engine)
}

expect fun createPlaybackEngine(): PlaybackEngine

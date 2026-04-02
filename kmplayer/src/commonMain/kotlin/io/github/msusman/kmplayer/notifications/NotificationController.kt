package io.github.msusman.kmplayer.notifications

import io.github.msusman.kmplayer.api.MediaItem

interface NotificationController {
    fun showNowPlaying(item: MediaItem)
    fun updatePlayback(isPlaying: Boolean)
    fun dismiss()
    fun release()
}

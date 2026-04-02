package io.github.msusman.kmplayer.analytics

interface AnalyticsTracker {
    fun track(eventName: String, params: Map<String, String> = emptyMap())
}

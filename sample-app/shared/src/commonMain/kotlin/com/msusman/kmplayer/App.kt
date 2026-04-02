package com.msusman.kmplayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.LocalPlatformContext
import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.KMPlayerBuilder
import io.github.msusman.kmplayer.api.PlayerListener
import io.github.msusman.kmplayer.api.PlayerState


@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val context: coil3.PlatformContext = LocalPlatformContext.current
        val player = remember(context) { KMPlayerBuilder().build(context) }
        val playerState = remember { mutableStateOf(player.currentState()) }
        val testItem = remember {
            MediaItem(
                id = "test-remote-1",
                uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                title = "Test MP3"
            )
        }

        DisposableEffect(player) {
            val listener = object : PlayerListener {
                override fun onStateChanged(state: PlayerState) {
                    playerState.value = state
                }
            }
            player.addListener(listener)
            onDispose { player.removeListener(listener) }
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) { Text("Click me!") }
            Text("Player state: ${playerState.value}")

            Row {
                Button(onClick = {
                    player.load(testItem, autoplay = true)
                }) {
                    Text("Play")
                }
                Button(onClick = { player.pause() }) {
                    Text("Pause")
                }
            }

            val (positionMs, durationMs) = extractProgress(playerState.value)
            Text("Progress: ${formatMs(positionMs)} / ${formatMs(durationMs)}")
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}

private fun extractProgress(state: PlayerState): Pair<Long, Long> = when (state) {
    is PlayerState.Ready -> state.positionMs to state.durationMs
    is PlayerState.Playing -> state.positionMs to state.durationMs
    is PlayerState.Paused -> state.positionMs to state.durationMs
    is PlayerState.Buffering -> state.positionMs to state.durationMs
    is PlayerState.Stopped -> state.positionMs to state.durationMs
    is PlayerState.Completed -> state.durationMs to state.durationMs
    else -> 0L to 0L
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt().coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val secondsPadded = if (seconds < 10) "0$seconds" else "$seconds"
    return "$minutes:$secondsPadded"
}

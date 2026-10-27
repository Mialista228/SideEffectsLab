package com.example.sideeffectslab

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun Task1Screen() {
    var version by remember { mutableStateOf(0) }
    var showVariant by remember { mutableStateOf(true) }

    // Version counter increasing every 500ms
    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            version++
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Version Counter: v$version", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Show side-by-side or toggled variants
            LandingScreenStale(onTimeout = { Log.d("Task1", "Stale variant called: v$version") })
            Spacer(modifier = Modifier.height(16.dp))
            LandingScreenUpdated(onTimeout = { Log.d("Task1", "Updated variant called: v$version") })
        }
    }
}

@Composable
fun LandingScreenStale(onTimeout: () -> Unit) {
    // BUG: Capturing lambda directly with LaunchedEffect(true) causes a stale capture
    LaunchedEffect(true) {
        delay(2000L)
        onTimeout() // Will print v0 because key is true and lambda was captured at initial composition
    }
    Text("Variant A: Stale Capture (Check Logcat)")
}

@Composable
fun LandingScreenUpdated(onTimeout: () -> Unit) {
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(true) {
        delay(2000L)
        currentOnTimeout() // Calls latest lambda reference without restarting the timer
    }
    Text("Variant B: rememberUpdatedState (Check Logcat)")
}

/*
 * COMMENT ON LAUNCHEDEFFECT(onTimeout):
 * If we change LaunchedEffect(true) to LaunchedEffect(onTimeout), every time the parent
 * recomposes and passes a new lambda reference (due to version increasing), the effect
 * key changes. This causes the coroutine to cancel and restart the 2-second delay
 * indefinitely, meaning onTimeout() would never actually fire.
 */
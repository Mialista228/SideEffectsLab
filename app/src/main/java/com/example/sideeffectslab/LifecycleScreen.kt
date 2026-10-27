package com.example.sideeffectslab

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun Task3Screen() {
    var showComposable by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Toggle Composable: ")
                Switch(checked = showComposable, onCheckedChange = { showComposable = it })
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (showComposable) {
                LifecycleLoggerComponent(lifecycleOwner = lifecycleOwner)
            }
        }
    }
}

@Composable
fun LifecycleLoggerComponent(lifecycleOwner: androidx.lifecycle.LifecycleOwner) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> Log.d("LifecycleTask", "ON_START triggered")
                Lifecycle.Event.ON_STOP -> Log.d("LifecycleTask", "ON_STOP triggered")
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        Log.d("LifecycleTask", "addObserver executed")

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d("LifecycleTask", "removeObserver executed (cleanup)")
        }
    }

    Text("Lifecycle Logger Active Component")
}

/*
 * BUILD ERROR NOTE (Temporary removal of onDispose):
 * If you remove the onDispose block, the Kotlin compiler throws:
 * "A DisposableEffect must provide an onDispose block as its final expression."
 * This guarantees that every resource registered is guaranteed to have cleanup logic.
 */
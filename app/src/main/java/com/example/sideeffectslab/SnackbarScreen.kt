package com.example.sideeffectslab

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Task2Screen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                // COROUTINE SCOPE USAGE:
                // LaunchedEffect cannot be called here because callbacks are imperative event handlers,
                // whereas LaunchedEffect is a Composable function that must be declared directly in the
                // Composition tree. rememberCoroutineScope binds the coroutine's lifetime to the Composable.
                scope.launch {
                    snackbarHostState.showSnackbar("Hello from Snackbar! Navigating away will cancel safely.")
                }
            }) {
                Text("Show Snackbar")
            }
        }
    }
}

/*
 * OBSERVATION ON MULTIPLE TAPS:
 * If you tap the button multiple times while a snackbar is already visible,
 * subsequent snackbar requests are queued or replace the current one depending
 * on SnackbarHostState implementation, without crashing the app or leaking jobs.
 * When the screen leaves the Composition, the scope is automatically cancelled.
 */
package com.example.sideeffectslab

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun Task5Screen() {
    val listState = rememberLazyListState()

    // Variant A: derivedStateOf for button visibility
    val showButtonDerived by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    // Variant B: Direct read for comparison
    val showButtonDirect = listState.firstVisibleItemIndex > 0

    // Recomposition counters
    var derivedRecompCount by remember { mutableIntStateOf(0) }
    var directRecompCount by remember { mutableIntStateOf(0) }

    // snapshotFlow analytics pipeline
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                Log.d("ScrollAnalytics", "Analytics Event: User scrolled past item 0!")
            }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) { index ->
                    Text(
                        text = "Item #$index",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Floating status card showing recomposition stats
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Track derived variant recompositions
                    SideEffect { derivedRecompCount++ }
                    // Track direct variant recompositions
                    SideEffect { directRecompCount++ }

                    Text("derivedStateOf Recompositions: $derivedRecompCount")
                    Text("Direct Read Recompositions: $directRecompCount")
                    Text("Button Visible (Derived): $showButtonDerived")
                }
            }
        }
    }
}
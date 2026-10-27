package com.example.sideeffectslab

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Simple external analytics object
object AnalyticsService {
    var userType: String = "Guest"
}

@Composable
fun Task4Screen() {
    var userType by remember { mutableStateOf("Standard") }
    var sideEffectRunCount by remember { mutableIntStateOf(0) }

    // SideEffect runs after every successful recomposition
    SideEffect {
        sideEffectRunCount++
        AnalyticsService.userType = userType
        Log.d("AnalyticsTask", "SideEffect ran count: $sideEffectRunCount with userType: $userType")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current User Type: $userType", style = MaterialTheme.typography.titleLarge)
            Text("SideEffect Run Count: $sideEffectRunCount", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { userType = if (userType == "Standard") "Premium" else "Standard" }) {
                Text("Toggle User Type")
            }
        }
    }
}

/*
 * OBSERVATION & ATTEMPT MODEL EXPLANATION:
 * SideEffect runs after *every successful commit*, not just when state changes. If a recomposition
 * occurs due to parent changes or UI ticks, SideEffect executes again to synchronize non-Compose state.
 * Lecture's Attempt Model: During recomposition, Compose can attempt multiple rendering passes.
 * Direct calls in the composable body fire even on discarded attempts (commits = false), leading to
 * duplicate or premature logs. SideEffect waits until commits = true (successfully committed to the UI tree).
 */
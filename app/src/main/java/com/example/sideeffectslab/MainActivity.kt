package com.example.sideeffectslab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sideeffectslab.ui.theme.SideEffectsLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SideEffectsLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SideEffectsLabApp()
                }
            }
        }
    }
}

enum class LabTask(val title: String) {
    TASK_1("Task 1: Splash Screen & rememberUpdatedState"),
    TASK_2("Task 2: Snackbar & rememberCoroutineScope"),
    TASK_3("Task 3: Lifecycle Logger & DisposableEffect"),
    TASK_4("Task 4: SideEffect & Analytics"),
    TASK_5("Task 5: derivedStateOf & snapshotFlow"),
    TASK_6("Task 6: produceState (Bonus)")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectsLabApp() {
    var currentTask by remember { mutableStateOf<LabTask?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTask?.title ?: "SideEffectsLab - Task Menu") },
                navigationIcon = {
                    if (currentTask != null) {
                        IconButton(onClick = { currentTask = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Menu"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (currentTask == null) {
                // Task Selection Menu
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(LabTask.values()) { task ->
                        Card(
                            onClick = { currentTask = task },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = task.title,
                                modifier = Modifier.padding(20.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            } else {
                // Render the selected task screen
                when (currentTask) {
                    LabTask.TASK_1 -> Task1Screen()
                    LabTask.TASK_2 -> TODO()
                    LabTask.TASK_3 -> TODO()
                    LabTask.TASK_4 -> TODO()
                    LabTask.TASK_5 -> TODO()
                    LabTask.TASK_6 -> TODO()
                    null -> {}
                }
            }
        }
    }
}
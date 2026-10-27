package com.example.sideeffectslab

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Sealed UI State representing image loading lifecycle
sealed interface ImageLoadState {
    object Loading : ImageLoadState
    data class Success(val url: String) : ImageLoadState
    object Error : ImageLoadState
}

// Fake repository function
suspend fun loadNetworkImageRepo(url: String): ImageLoadState {
    delay(500L) // Simulate network latency
    return if (url.endsWith(".png")) {
        ImageLoadState.Success(url)
    } else {
        ImageLoadState.Error
    }
}

// Custom produceState wrapper function following convention (lowercase start)
@Composable
fun loadNetworkImage(url: String): State<ImageLoadState> {
    return produceState<ImageLoadState>(initialValue = ImageLoadState.Loading, key1 = url) {
        try {
            // value is the underlying MutableState value
            value = ImageLoadState.Loading
            value = loadNetworkImageRepo(url)
        } finally {
            Log.d("ProduceStateTask", "Producer cancelled for URL: $url (Composable left)")
        }
    }
}

@Composable
fun Task6Screen() {
    var url by remember { mutableStateOf("https://example.com/image.png") }
    val imageState by loadNetworkImage(url)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // UI handled with a when expression
            when (val state = imageState) {
                is ImageLoadState.Loading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Loading image...")
                }
                is ImageLoadState.Success -> {
                    Text("Success! Loaded: ${state.url}", color = MaterialTheme.colorScheme.primary)
                }
                is ImageLoadState.Error -> {
                    Text("Error: Invalid image format", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                url = if (url.endsWith(".png")) "https://example.com/invalid.jpg" else "https://example.com/image.png"
            }) {
                Text("Toggle URL (Test Restart & Conflation)")
            }
        }
    }
}
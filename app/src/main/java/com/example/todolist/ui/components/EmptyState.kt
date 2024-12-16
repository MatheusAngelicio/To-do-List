package com.example.todolist.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolist.ui.theme.TodoListTheme

@Composable
fun EmptyState(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = Color.WHITE.toLong()
)
@Composable
private fun EmptyStatePreview() {
    TodoListTheme {
        EmptyState("Something is empty")
    }
}

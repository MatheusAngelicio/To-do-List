package com.example.todolist.ui.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.TodoListTheme

@Composable
fun AddEditScreen() {
    AddEditContent()
}

@Composable
fun AddEditContent() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save ")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    // Pra preencher toda largura disponivel
                    .fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Title")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    // Pra preencher toda largura disponivel
                    .fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Description (optional)")
                }
            )

        }

    }

}

@Preview
@Composable
private fun AddEditContentPreview() {
    TodoListTheme {
        AddEditContent()
    }
}
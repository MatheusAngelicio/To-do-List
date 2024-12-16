package com.example.todolist.ui.feature.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.TodoDatabaseProvider
import com.example.todolist.data.TodoRepositoryImpl
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.theme.TodoListTheme

@Composable
fun AddEditScreen(
    id: Long?,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    val dataBase = TodoDatabaseProvider.provide(context)
    val repository = TodoRepositoryImpl(dataBase.dao)

    val viewModel = viewModel<AddEditViewModel> {
        AddEditViewModel(id = id, repository = repository)
    }

    val title = viewModel.title
    val description = viewModel.description

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = uiEvent.message)
                }

                UiEvent.NavigateBack -> {
                    navigateBack()
                }

                // como estamos esperando um tipo generico aqui pra escutar as mudanças, eu posso passar o * pra funcionar corretamente
                is UiEvent.Navigate<*> -> {}
            }

        }

    }

    AddEditContent(
        title = title,
        description = description,
        topBarTitle = id?.let { "Update" } ?: "Create",
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBack = viewModel::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContent(
    title: String,
    description: String?,
    topBarTitle : String,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddEditEvent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(topBarTitle, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AddEditEvent.Save)
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save ")
            }
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }


    ) { padding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(padding)
                // Aplica o padding fornecido pelo Scaffold (nesse caso uso para que a topbar nao fique na frente do conteudo da tela)
                .padding(padding)
                // aqui um padding adicional para tela
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    // Pra preencher toda largura disponivel
                    .fillMaxWidth(),
                value = title,
                onValueChange = {
                    onEvent(AddEditEvent.TitleChanged(it))
                },
                placeholder = {
                    Text("Title")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    // Pra preencher toda largura disponivel
                    .fillMaxWidth(),
                value = description ?: "",
                onValueChange = {
                    onEvent(AddEditEvent.DescriptionChanged(it))
                },
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
        AddEditContent(
            title = "",
            description = null,
            topBarTitle = "Create",
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBack = {}
        )
    }
}
package com.example.todolist.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoRepository
import com.example.todolist.domain.Todo
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    val todos = repository.getAll()
        .stateIn(
            scope = viewModelScope, // Define o escopo do fluxo como o viewModelScope, encerrando-o automaticamente quando o ViewModel for destruído.
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), // Mantém o fluxo ativo enquanto houver assinantes, com um timeout de 5 segundos após o último cancelar.
            initialValue = emptyList() // Define uma lista vazia como valor inicial até que os dados reais sejam emitidos pelo repositório.
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.Delete -> {
                delete(event.todo)
            }

            is ListEvent.CompletedChanged -> {
                completedChanged(event.todo, event.completed)
            }

            is ListEvent.AddEdit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddEditRoute(event.id)))
                }
            }
        }
    }

    private fun delete(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo.id)
            _uiEvent.send(UiEvent.ShowSnackbar("Todo ${todo.title} deleted !"))
        }
    }

    private fun completedChanged(todo: Todo, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateCompleted(todo.id, isCompleted)
        }
    }

}
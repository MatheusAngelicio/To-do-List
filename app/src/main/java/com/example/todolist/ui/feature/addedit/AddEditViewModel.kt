package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoRepository
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val repository: TodoRepository,
) : ViewModel() {

    // 'private set' para que apenas meu AddEditViewModel consiga modificar o valor de title
    var title by mutableStateOf("")
        private set


    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    // quando esse viewModel for criado
    init {
        id?.let { todoId ->
            viewModelScope.launch {
                // repository.getBy pode vir null se nao achar um toodo com esse id no banco, no meu contexto isso nao deve acontecer mas pro codigo nao ficar com erro tive que por o ?.let
                repository.getBy(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                title = event.title
            }

            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }

            AddEditEvent.Save -> {
                saveTodo()
            }

        }
    }

    private fun saveTodo() {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("Title cannot be empty"))
                // retorno pra nao deixar executar o repository.insert (pra nao salvar no banco)
                return@launch
            }
            repository.insert(title, description, id)
            onBack()
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }
}
package com.example.todolist.ui

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent

    // data object por que a gente nao recebe nenhum parametro
    data object NavigateBack : UiEvent

    data class Navigate<T : Any>(val route: T) : UiEvent

}
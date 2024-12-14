package com.example.todolist.ui.feature.list

import com.example.todolist.domain.Todo

sealed interface ListEvent {
    data class Delete(val todo: Todo) : ListEvent
    data class CompletedChanged(val todo: Todo, val completed: Boolean) : ListEvent
    data class AddEdit(val id: Long?) : ListEvent

}
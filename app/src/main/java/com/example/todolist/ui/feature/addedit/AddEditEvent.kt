package com.example.todolist.ui.feature.addedit


// essa classe selada representa as açoes que minha tela vai ter
sealed interface AddEditEvent {
    data class TitleChanged(val title: String) : AddEditEvent
    data class DescriptionChanged(val description: String) : AddEditEvent
    data object Save : AddEditEvent

}
package com.example.latihanmvvm

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val titleError: String? = null
)
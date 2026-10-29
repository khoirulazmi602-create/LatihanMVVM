package com.example.latihanmvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    private var nextId = 1

    // Event: pengguna mengetik judul
    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle, titleError = null) }
    }

    // Event: pengguna mengetik isi
    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent) }
    }

    // Event: pengguna menekan Simpan
    fun addNote() {
        val current = _uiState.value

        if (current.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Judul tidak boleh kosong") }
            return
        }

        val newNote = Note(nextId++, current.title.trim(), current.content.trim())

        // Tambah catatan, lalu kosongkan input dan error
        _uiState.update {
            it.copy(
                notes = it.notes + newNote,
                title = "",
                content = "",
                titleError = null
            )
        }
    }

    // Event: pengguna menekan Hapus
    fun deleteNote(note: Note) {
        _uiState.update { it.copy(notes = it.notes - note) }
    }
}
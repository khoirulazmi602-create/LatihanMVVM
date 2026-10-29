package com.example.latihanmvvm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// ===== LAYAR UTAMA: membaca state, memilih susunan layout =====
@Composable
fun NoteScreen(viewModel: NoteViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // BoxWithConstraints = tahu lebar yang tersedia (maxWidth)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        if (maxWidth < 600.dp) {
            // LAYAR SEMPIT: form di atas, daftar di bawah (bertumpuk)
            Column(modifier = Modifier.fillMaxSize()) {
                NoteForm(
                    uiState = uiState,
                    onTitleChange = viewModel::onTitleChange,
                    onContentChange = viewModel::onContentChange,
                    onSave = viewModel::addNote,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                NoteList(
                    notes = uiState.notes,
                    onDelete = viewModel::deleteNote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        } else {
            // LAYAR LEBAR: form di kiri, daftar di kanan (berdampingan)
            Row(modifier = Modifier.fillMaxSize()) {
                NoteForm(
                    uiState = uiState,
                    onTitleChange = viewModel::onTitleChange,
                    onContentChange = viewModel::onContentChange,
                    onSave = viewModel::addNote,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )
                Spacer(Modifier.width(16.dp))
                NoteList(
                    notes = uiState.notes,
                    onDelete = viewModel::deleteNote,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

// ===== FORM INPUT =====
@Composable
fun NoteForm(
    uiState: NoteUiState,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            label = { Text("Judul (wajib diisi)") },
            isError = uiState.titleError != null,
            supportingText = {
                uiState.titleError?.let { Text(it) }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.content,
            onValueChange = onContentChange,
            label = { Text("Isi catatan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
            Text("Simpan")
        }
    }
}

// ===== DAFTAR CATATAN (memilih: kosong atau ada data) =====
@Composable
fun NoteList(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notes.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "Belum ada catatan.\nIsi judul lalu tekan Simpan.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                NoteItem(note = note, onDelete = { onDelete(note) })
            }
        }
    }
}

// ===== SATU KARTU CATATAN =====
@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium)
                if (note.content.isNotBlank()) {
                    Text(note.content)
                }
            }
            TextButton(onClick = onDelete) {
                Text("Hapus")
            }
        }
    }
}
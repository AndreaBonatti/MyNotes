package com.andreabonatti92.mynotes.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.ApiRepository
import com.andreabonatti92.mynotes.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _noteListState = MutableStateFlow<NoteListState>(NoteListState.Loading)
    val noteListState: StateFlow<NoteListState> = _noteListState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _noteListState.value = NoteListState.Loading
            val result = apiRepository.getNotes()
            _noteListState.value = result.fold(
                onSuccess = { NoteListState.Success(it) },
                onFailure = { NoteListState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}

sealed class NoteListState {
    data object Loading : NoteListState()
    data class Success(val notes: List<Note>) : NoteListState()
    data class Error(val message: String) : NoteListState()
}
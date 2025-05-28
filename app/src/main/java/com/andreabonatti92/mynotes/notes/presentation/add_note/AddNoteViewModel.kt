package com.andreabonatti92.mynotes.notes.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.ApiRepository
import com.andreabonatti92.mynotes.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _addNoteState = MutableStateFlow<AddNoteState>(AddNoteState.Idle)
    val addNoteState: StateFlow<AddNoteState> = _addNoteState.asStateFlow()

    fun addNote(title: String, content: String, color: Int) {
        if (_addNoteState.value is AddNoteState.Uploading) return

        viewModelScope.launch {
            _addNoteState.value = AddNoteState.Uploading
            try {
                val result = apiRepository.insertNote(title, content, color)
                _addNoteState.value = result.fold(
                    onSuccess = { AddNoteState.Success(it) },
                    onFailure = { AddNoteState.Error(it.message ?: "Unknown error") }
                )
            } catch (e: Exception) {
                _addNoteState.value = AddNoteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _addNoteState.value = AddNoteState.Idle
    }
}

sealed class AddNoteState {
    data object Idle : AddNoteState()
    data object Uploading : AddNoteState()
    data class Success(val note: Note) : AddNoteState()
    data class Error(val message: String) : AddNoteState()
}
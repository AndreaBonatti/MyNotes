package com.andreabonatti92.mynotes.notes.presentation.note_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.ApiRepository
import com.andreabonatti92.mynotes.notes.domain.Note
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    var notes by mutableStateOf<List<Note>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            isLoading = true
            val result = apiRepository.getNotes()
            result.onSuccess {
                notes = it
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Unknown error"
            }
            isLoading = false
        }
    }
}
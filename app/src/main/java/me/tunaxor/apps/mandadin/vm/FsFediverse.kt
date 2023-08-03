package me.tunaxor.apps.mandadin.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.tunaxor.apps.mandadin.services.FsFediverseService
import me.tunaxor.apps.mandadin.types.FsFedNote

enum class NotesPageDirection {
    Initial,
    Next,
    Back
}

class FsFedNotesVm(val service: FsFediverseService) : ViewModel() {

    var notes: Set<FsFedNote> by mutableStateOf(emptySet())
        private set

    var page by mutableStateOf(1)
        private set
    var limit by mutableStateOf(10)


    suspend fun loadNotes(direction: NotesPageDirection = NotesPageDirection.Initial) {
        when(direction) {
            NotesPageDirection.Initial -> fetchNotes(1, limit)
            NotesPageDirection.Next -> next()
            NotesPageDirection.Back -> back()
        }
    }

    private suspend fun next() {
        page += 1
        fetchNotes(page, limit)
    }

    private suspend fun back() {
        if(page == 1) {
            return
        }
        page -= 1
        fetchNotes(page, limit)
    }

    suspend fun fetchNote(note: String): FsFedNote? {
        return service.findOne(note)
    }


    private suspend fun fetchNotes(page: Int, limit: Int) {
        val _notes =  service.find(page, limit)
        notes = _notes.toSet()
    }

}
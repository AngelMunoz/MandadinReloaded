package me.tunaxor.apps.mandadin.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.tunaxor.apps.mandadin.services.FsFediverseService
import me.tunaxor.apps.mandadin.types.FsFedNote

class FsFedNotesVm(val service: FsFediverseService) : ViewModel() {
    var notes: List<FsFedNote> by mutableStateOf(emptyList())
        private set

    suspend fun fetchNotes(page: Int? = null, limit: Int? = null) {
        notes = service.find(page ?: 1, limit ?: 10)
    }

    suspend fun fetchNote(note: String): FsFedNote? {
        return service.findOne(note)
    }
}
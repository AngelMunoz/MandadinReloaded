package me.tunaxor.apps.mandadin.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tunaxor.apps.mandadin.types.FsFedNote
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm


@Composable
fun FsFediverseNotesPage(vm: FsFedNotesVm = viewModel()) {
    Text("Fs Fediverse Notes!")
}

@Composable
fun FsFediverseNotePage(noteId: String?, vm: FsFedNotesVm = viewModel()) {
    var note: FsFedNote? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        if(noteId != null)
            note = vm.fetchNote(noteId)
    }

    Text("Fs Fediverse Notes!")
}
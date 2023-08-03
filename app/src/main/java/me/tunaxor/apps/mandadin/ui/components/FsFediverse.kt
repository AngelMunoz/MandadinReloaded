package me.tunaxor.apps.mandadin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.tunaxor.apps.mandadin.types.FsFedNote
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm
import me.tunaxor.apps.mandadin.vm.NotesPageDirection


@Composable
fun FsNavPagination(vm: FsFedNotesVm = viewModel()) {
    val scope = rememberCoroutineScope()
    BottomAppBar {
        IconButton(onClick = { scope.launch { vm.loadNotes(NotesPageDirection.Back) } }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Previous Page",
                modifier = Modifier.padding(4.dp),
            )
        }
        Text(text = "Page: ${vm.page}")
        IconButton(onClick = { scope.launch { vm.loadNotes(NotesPageDirection.Next) } }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Previous Page",
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun FsNoteNav(onBackRequested: () -> Unit) {
    BottomAppBar {
        TextButton(onClick = onBackRequested) {
            Text("Go back")
        }
    }
}

@Composable
fun FsFediverseNoteView(
    note: FsFedNote, modifier: Modifier = Modifier, onNoteRequested: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), text = note.text
            )

            TextButton(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
                onClick = { onNoteRequested(note.id) }) {
                Text("${note.text.take(20)}...")
            }
        }

    }
}


@Composable
fun FsFediverseNotesPage(navController: NavController, vm: FsFedNotesVm = viewModel()) {
    LazyColumn {
        items(vm.notes.toList(), key = { it.id }) { note ->
            FsFediverseNoteView(note = note,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onNoteRequested = { navController.navigate("notes/$it") })
        }
    }
}

@Composable
fun FsFediverseNotePage(
    noteId: String?, vm: FsFedNotesVm = viewModel(), onNoteRequested: (String) -> Unit
) {
    var note: FsFedNote? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        if (noteId != null) note = vm.fetchNote(noteId)
    }

    if (note != null) {
        FsFediverseNoteView(note = note!!, onNoteRequested = onNoteRequested)
    } else {
        Text("No Note was selected")
    }
}
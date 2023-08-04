package me.tunaxor.apps.mandadin.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch
import me.tunaxor.apps.mandadin.types.FsFedNote
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm
import me.tunaxor.apps.mandadin.vm.NotesPageDirection
import java.text.DateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


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
fun FsNoteHeader() {

}

@Composable
fun FsNoteFooter(
    title: String, postedAt: String, noteId: String, onNoteRequested: (String) -> Unit
) {

    Row {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .weight(2f),
            onClick = { onNoteRequested(noteId) },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("${title.take(20)}...")
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .weight(1f),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
        ) {
            val parsedDate = OffsetDateTime.parse(postedAt, DateTimeFormatter.ISO_DATE_TIME)
            val text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date.from(parsedDate.toInstant()))
            Text(text = text)
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
            if (note.reply != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    FsFediverseNoteView(note = note.reply, onNoteRequested = onNoteRequested)
                }
            }

            MarkdownText(
                markdown = note.text,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            FsNoteFooter(
                title = note.text,
                postedAt = note.createdAt,
                noteId = note.id,
                onNoteRequested = onNoteRequested
            )
        }

    }
}


@Composable
fun FsFediverseNotesPage(navController: NavController, vm: FsFedNotesVm = viewModel()) {
    BackHandler {
        navController.navigateUp()
    }
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
    noteId: String?,
    vm: FsFedNotesVm = viewModel(),
    onBackRequested: () -> Unit = {},
    onNoteRequested: (String) -> Unit
) {
    var note: FsFedNote? by remember { mutableStateOf(null) }

    BackHandler {
        onBackRequested()
    }

    LaunchedEffect(true) {
        if (noteId != null) note = vm.fetchNote(noteId)
    }

    if (note != null) {
        FsFediverseNoteView(note = note!!, onNoteRequested = onNoteRequested)
    } else {
        Text("No Note was selected")
    }
}
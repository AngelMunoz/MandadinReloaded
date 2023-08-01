package me.tunaxor.apps.mandadin.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FormLabel(title: String) {
    Text(title, Modifier.padding(2.dp))
}

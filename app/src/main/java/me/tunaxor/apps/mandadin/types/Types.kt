package me.tunaxor.apps.mandadin.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Todo(
    val id: UUID,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val tags: List<String>,
): Parcelable


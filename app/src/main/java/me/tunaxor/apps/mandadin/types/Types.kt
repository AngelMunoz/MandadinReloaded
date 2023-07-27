package me.tunaxor.apps.mandadin.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val tags: List<String>,
): Parcelable


package me.tunaxor.apps.mandadin.types


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.UUID

@Parcelize
data class Todo(
    val id: UUID,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val tags: List<String>,
) : Parcelable

@Parcelize
@Serializable
data class FsFedInstance(
    val name: String,
    val softwareName: String,
    val softwareVersion: String,
    val iconUrl: String,
    val faviconUrl: String,
    val themeColor: String
) : Parcelable

@Parcelize
@Serializable
data class MkUser(val username: String, val host: String?, val instance: FsFedInstance?) :
    Parcelable

@Parcelize
@Serializable
data class FsFedNote(
    val id: String,
    val createdAt: String,
    val url: String,
    val reactions: Map<String, Long>,
    val text: String,
    val user: MkUser,
    val reply: FsFedNote?
) : Parcelable
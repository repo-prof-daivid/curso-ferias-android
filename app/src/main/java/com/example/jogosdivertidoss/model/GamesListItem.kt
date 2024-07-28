package com.example.jogosdivertidoss.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamesListItem(
    var id: String = "",
    var name: String = "",
    var type: String = "",
    var durationtime: String = "",
    var grade: String = "",
    var finished: Boolean = false
): Parcelable


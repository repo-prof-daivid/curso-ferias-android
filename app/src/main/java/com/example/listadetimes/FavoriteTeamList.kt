package com.example.listadetimes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteTeamList(
    var id: String = "",
    var nameTeam: String = "",
    var stadiumAudience: String = "",
    var mascot: String = "",
    var city: String = "",
    var foundationYear: String = "",
    var favoriteTeam: Boolean = false
): Parcelable
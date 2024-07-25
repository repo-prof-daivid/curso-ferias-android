package com.example.listadeanimes.Model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AnimeLista (
    var id : String = "",
    var tittle : String = "",
    var releaseyear  : String = "",
    var episodes : String  = "",
    var gender : String   = "",
    var isChecked: Boolean = false

): Parcelable



package com.example.listadecompras

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val name: String,
    val qtd: String,
    val marca: String
): Parcelable
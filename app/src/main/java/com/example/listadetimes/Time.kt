package com.example.listadetimes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val nomeTime: String,
    val qtdPublico: String,
    val cidade: String,
    val fundacaoAno : Int,
    val mascote: String
): Parcelable
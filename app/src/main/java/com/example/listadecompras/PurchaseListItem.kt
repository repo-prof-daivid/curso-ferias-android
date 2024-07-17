package com.example.listadecompras

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseListItem(
    var id: String = "",
    var name: String = "",
    var qtd: String = "",
    var measurementUnit: String = "",
    var band: String = "",
    var purchased: Boolean = false
): Parcelable
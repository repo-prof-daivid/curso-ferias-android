package com.example.listadecompras

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uId: String,
    var name: String,
    var email: String
): Parcelable
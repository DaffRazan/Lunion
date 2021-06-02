package com.lunion.lunionapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val uid: String? = null,
    val fullname: String? = null,
    val email: String? = null,
    val type: String? = null
): Parcelable

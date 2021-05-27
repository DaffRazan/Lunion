package com.lunion.lunionapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var id: Int = 0,
    var content: List<NewsItem>
) : Parcelable
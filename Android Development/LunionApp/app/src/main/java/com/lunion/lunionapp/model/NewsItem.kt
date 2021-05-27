package com.lunion.lunionapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    var id: Int = 0,
    var news_image: String = "",
    var title: String = "",
    var source: String = "",
    var date: String = "",
    var content: String = ""
) : Parcelable
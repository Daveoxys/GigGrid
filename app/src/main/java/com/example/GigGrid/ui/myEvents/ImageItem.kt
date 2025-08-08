package com.example.GigGrid.ui.myEvents

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItem (
    val uri: Uri,
    val bandEvent: String,
    val location: String,
    val date: String
) : Parcelable
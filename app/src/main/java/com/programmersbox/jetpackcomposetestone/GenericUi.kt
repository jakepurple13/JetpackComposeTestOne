package com.programmersbox.jetpackcomposetestone

import androidx.activity.ComponentActivity
import androidx.compose.material.Colors
import androidx.compose.runtime.MutableState

interface GenericInfo {
    val nameToUse: String
    fun getItems(page: Int): List<GenericData>
}

abstract class GenericData(
    open val title: String,
    open val url: String,
) {
    abstract val listOfData: GenericInformation?
}

abstract class GenericInformation(
    open val title: String,
    open val url: String,
    open val imageUrl: String?,
    open val description: String?,
    open val genres: List<String>,
    open val enumSource: GenericInfo
) : GenericUi {
    abstract fun rowData(): List<RowData>
}

class RowData(val name: String, val uploaded: String, val getItem: () -> List<String>)

fun interface GenericUi {
    fun playUi(activity: ComponentActivity?, list: RowData, title: String)
}
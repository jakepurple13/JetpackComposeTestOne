package com.programmersbox.jetpackcomposetestone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> LazyGridFor(
    items: List<T>,
    rowSize: Int = 1,
    modifier: Modifier = Modifier,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val rows = items.chunked(rowSize)
    LazyColumnFor(rows) { row ->
        Row(Modifier.fillParentMaxWidth()) {
            for ((index, item) in row.withIndex()) {
                Box(
                    Modifier.fillMaxWidth(1f / (rowSize - index)).then(modifier),
                    alignment = Alignment.TopCenter
                ) {
                    itemContent(item)
                }
            }
        }
    }
}
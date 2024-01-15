package com.alexkva.calculadorafinanciamento.utils.classes

import androidx.compose.ui.text.input.OffsetMapping

class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return formattedContentLength
    }

    override fun transformedToOriginal(offset: Int): Int {
        return contentLength
    }
}
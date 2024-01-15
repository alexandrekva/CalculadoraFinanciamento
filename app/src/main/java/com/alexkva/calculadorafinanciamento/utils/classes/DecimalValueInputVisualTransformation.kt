package com.alexkva.calculadorafinanciamento.utils.classes

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.alexkva.calculadorafinanciamento.utils.extensions.toBigDecimalFromInput
import com.alexkva.calculadorafinanciamento.utils.extensions.toInputString

class DecimalValueInputVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val transformedText = text.text.takeIf { it.isNotEmpty() }?.run {
            toBigDecimalFromInput()?.toInputString() ?: ERROR_STRING
        } ?: EMPTY_STRING

        val annotatedString = AnnotatedString(
            text = transformedText,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = FixedCursorOffsetMapping(
            contentLength = text.length,
            formattedContentLength = transformedText.length
        )

        return TransformedText(text = annotatedString, offsetMapping = offsetMapping)
    }

    companion object {
        private const val ERROR_STRING = "ERROR"
        private const val EMPTY_STRING = ""
    }
}
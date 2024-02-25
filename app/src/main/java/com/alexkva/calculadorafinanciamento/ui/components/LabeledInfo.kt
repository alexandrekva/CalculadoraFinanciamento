package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LabeledInfo(
    modifier: Modifier = Modifier,
    label: String,
    info: String,
    labelColor: Color = Color.Unspecified,
    infoColor: Color = Color.Unspecified
) {
    val definedLabelColor = labelColor.takeOrElse {
        LocalContentColor.current
    }

    val definedInfoColor = infoColor.takeOrElse {
        LocalContentColor.current
    }

    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = definedLabelColor
        )
        Text(
            text = info,
            style = MaterialTheme.typography.headlineMedium,
            color = definedInfoColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLabeledInfo() {
    LabeledInfo(label = "Valor total", info = "R$ 600.000,00")
}
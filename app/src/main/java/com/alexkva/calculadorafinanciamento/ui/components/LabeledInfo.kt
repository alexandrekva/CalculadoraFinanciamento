package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LabeledInfo(modifier: Modifier = Modifier, label: String, info: String) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = info,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight(600)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLabeledInfo() {
    LabeledInfo(label = "Valor total", info ="R$ 600.000,00" )
}
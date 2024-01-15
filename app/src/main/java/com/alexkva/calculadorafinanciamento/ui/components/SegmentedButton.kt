package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonModel
import com.alexkva.calculadorafinanciamento.ui.theme.EndSegmentedButtonShape
import com.alexkva.calculadorafinanciamento.ui.theme.StartSegmentedButtonShape

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    buttons: List<SegmentedButtonModel>,
    selectedButton: String,
    onButtonClick: (String) -> Unit
) {
    val size = buttons.size
    val buttonWeight = 1f / size.toFloat()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        buttons.forEachIndexed { index: Int, button: SegmentedButtonModel ->
            val isSelected = button.label == selectedButton

            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }

            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            val modifierSettings = when (index) {
                0 -> {
                    Modifier
                        .clip(StartSegmentedButtonShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f))
                        .padding(start = 1.dp, top = 1.dp, end = 0.5.dp, bottom = 1.dp)
                        .clip(StartSegmentedButtonShape)
                        .background(backgroundColor)
                }

                buttons.size - 1 -> {
                    Modifier
                        .clip(EndSegmentedButtonShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f))
                        .padding(start = 0.5.dp, top = 1.dp, end = 1.dp, bottom = 1.dp)
                        .clip(EndSegmentedButtonShape)
                        .background(backgroundColor)
                }

                else -> {
                    Modifier
                        .clip(RectangleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f))
                        .padding(start = 0.5.dp, top = 1.dp, end = 0.5.dp, bottom = 1.dp)
                        .clip(RectangleShape)
                        .background(backgroundColor)
                }
            }

            Box(
                Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .weight(buttonWeight)
                    .then(modifierSettings)
                    .clickable { onButtonClick(button.label) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CompositionLocalProvider(
                        LocalContentColor provides contentColor
                    ) {
                        button.icon?.let {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = it),
                                contentDescription = null
                            )
                        }
                        Text(text = button.label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSegmentedButton() {
    Column(modifier = Modifier.padding(24.dp)) {
        SegmentedButton(buttons = List(2) { i ->
            SegmentedButtonModel(
                "Botão ${i + 1}",
                R.drawable.ic_android_black_24dp
            )
        }, selectedButton = "Botão 1") {

        }
    }

}
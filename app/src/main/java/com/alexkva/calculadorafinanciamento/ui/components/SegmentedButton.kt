package com.alexkva.calculadorafinanciamento.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.R
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonCollection
import com.alexkva.calculadorafinanciamento.ui.models.SegmentedButtonModel
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    buttonCollection: SegmentedButtonCollection,
    selectedButton: Int,
    onButtonClick: (Int) -> Unit
) {
    val size = buttonCollection.segmentedButtons.size
    val buttonWeight = 1f / size.toFloat()

    val selectedButtonBackgroundColor = MaterialTheme.colorScheme.primaryContainer
    val unselectedButtonBackgroundColor = Color.Transparent

    val selectedButtonContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val unselectedButtonContentColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(100))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = (RoundedCornerShape(100))
            )
            .selectableGroup()
    ) {
        buttonCollection.segmentedButtons.forEachIndexed { index, button ->
            val isSelected = index == selectedButton

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(buttonWeight)
                    .background(if (isSelected) selectedButtonBackgroundColor else unselectedButtonBackgroundColor)
                    .clickable { onButtonClick(index) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                button.icon?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = it),
                        contentDescription = null,
                        tint = if (isSelected) selectedButtonContentColor else unselectedButtonContentColor
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = button.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) selectedButtonContentColor else unselectedButtonContentColor
                )
            }
            if (size > 1 && index != buttonCollection.segmentedButtons.lastIndex) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSegmentedButton() {
    CalculadoraFinanciamentoTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(24.dp)) {
            SegmentedButton(buttonCollection = SegmentedButtonCollection(
                List(3) { i ->
                    SegmentedButtonModel(
                        "Botão ${i + 1}",
                        R.drawable.ic_android_black_24dp
                    )
                }
            ), selectedButton = 0) {
            }
        }
    }
}
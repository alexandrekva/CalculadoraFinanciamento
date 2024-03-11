package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.ui.models.CompareItem
import com.alexkva.calculadorafinanciamento.ui.models.CompareTableCollection
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme

@Composable
fun CompareTable(
    modifier: Modifier = Modifier, compareTableCollection: CompareTableCollection
) {
    val compareColumnWeight = 1f / compareTableCollection.compareItemCollection.size

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(compareTableSpacer))
            compareTableCollection.rowLabelList.forEach {
                Box(
                    modifier = Modifier
                        .height(compareTableItemSize)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(0.7f)
                .clip(RoundedCornerShape(roundedBorderRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            compareTableCollection.compareItemCollection.forEachIndexed { index1, compareItem ->
                Column(modifier = Modifier.weight(compareColumnWeight)) {
                    val labelShape = when (index1) {
                        0 -> RoundedCornerShape(
                            topStart = roundedBorderRadius,
                            topEnd = smallBorderRadius,
                            bottomStart = smallBorderRadius,
                            bottomEnd = smallBorderRadius
                        )

                        compareItem.values.lastIndex -> RoundedCornerShape(
                            topStart = smallBorderRadius,
                            topEnd = roundedBorderRadius,
                            bottomStart = smallBorderRadius,
                            bottomEnd = smallBorderRadius
                        )

                        else -> RoundedCornerShape(
                            topStart = smallBorderRadius,
                            topEnd = smallBorderRadius,
                            bottomStart = smallBorderRadius,
                            bottomEnd = smallBorderRadius
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(compareTableColumnLabelSize)
                            .fillMaxWidth()
                            .padding(1.dp)
                            .clip(labelShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = compareItem.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    compareItem.values.forEachIndexed { index2, value ->

                        val valueShape = when {
                            index1 == 0 && index2 == compareItem.values.lastIndex -> RoundedCornerShape(
                                topStart = smallBorderRadius,
                                topEnd = smallBorderRadius,
                                bottomStart = roundedBorderRadius,
                                bottomEnd = smallBorderRadius
                            )

                            index1 == compareTableCollection.compareItemCollection.lastIndex && index2 == compareItem.values.lastIndex ->
                                RoundedCornerShape(
                                    topStart = smallBorderRadius,
                                    topEnd = smallBorderRadius,
                                    bottomStart = smallBorderRadius,
                                    bottomEnd = roundedBorderRadius
                                )

                            else -> RoundedCornerShape(smallBorderRadius)
                        }

                        Box(
                            modifier = Modifier
                                .height(compareTableItemSize)
                                .fillMaxWidth()
                                .padding(1.dp)
                                .clip(valueShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(start = 8.dp), contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private val compareTableSpacer = 35.dp
private val compareTableColumnLabelSize = 35.dp
private val compareTableItemSize = 70.dp
private val roundedBorderRadius = 12.dp
private val smallBorderRadius = 4.dp

@Preview
@Composable
private fun PreviewCompareTable() {
    CalculadoraFinanciamentoTheme {
        CompareTable(
            compareTableCollection = CompareTableCollection(
                rowLabelList = listOf(
                    "Teste 1", "Teste 2", "Teste 3"
                ), compareItemCollection = listOf(
                    CompareItem("tabela 1", listOf("1", "2", "3")),
                    CompareItem("tabela 2", listOf("1", "2", "3"))
                )
            )
        )
    }
}
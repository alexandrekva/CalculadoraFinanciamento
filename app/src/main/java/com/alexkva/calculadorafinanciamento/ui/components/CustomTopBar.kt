package com.alexkva.calculadorafinanciamento.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexkva.calculadorafinanciamento.ui.theme.CalculadoraFinanciamentoTheme

@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    contentColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    title: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val definedContainerColor = containerColor.takeOrElse {
        MaterialTheme.colorScheme.primary
    }

    val definedContentColor = contentColor.takeOrElse {
        MaterialTheme.colorScheme.onPrimary

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(definedContainerColor)
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides definedContentColor,
            LocalTextStyle provides MaterialTheme.typography.titleMedium
        ) {
            Row(modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                leadingIcon?.let {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                title?.let { title() }
            }

            trailingIcon?.let { trailingIcon() }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun PreviewCustomTopBar() {
    CalculadoraFinanciamentoTheme {
        CustomTopBar(
            leadingIcon = { Icon(imageVector = Icons.Rounded.Edit, contentDescription = null) },
            title = { Text(text = "Titulo") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            })
    }
}
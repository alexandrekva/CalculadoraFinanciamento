package com.alexkva.calculadorafinanciamento.ui.models

import androidx.compose.runtime.Immutable

data class MenuItemModel(val label: String)

@Immutable
data class MenuItemCollection(val menuItems: List<MenuItemModel>)